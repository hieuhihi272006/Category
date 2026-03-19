package com.javaweb.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.javaweb.model.builder.MoMoItem;
import com.javaweb.model.dto.BuyDTO;
import com.javaweb.model.entity.ProductVariantEntity;
import com.javaweb.repository.ProductVariantRepository;
import com.javaweb.service.PaymentService;
import com.javaweb.utils.HmacUtil;

@Service
public  class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private ProductVariantRepository productVariantRepository;


	private final String partnerCode = "MOMOXJRY20260128_TEST"; 
	private final String apiEndpoint = "https://test-payment.momo.vn/v2/gateway/api/create";
	private final String accessKey = "6ytws7r24I6u1Eb3";
	private final String secretKey = "ljfcBxHe4Dkteadam6GRHuF5b7IWzllr";
	
	@Override
	public Map<String,Object> buyProducts(List<BuyDTO> buyDTO) {
		// TODO Auto-generated method stub
		
		String requestId = UUID.randomUUID().toString();
		String orderId = UUID.randomUUID().toString();
		
		Long amount = 0L;
		
		List<MoMoItem> items = new ArrayList<>();
		
		for(BuyDTO it : buyDTO) {
			ProductVariantEntity variant = productVariantRepository.findById(it.getIdVariant()).get();
			MoMoItem momoItem = new MoMoItem();
			momoItem.setId(variant.getId().toString());
			momoItem.setName(variant.getProduct().getName());
			momoItem.setPrice(variant.getPrice());
			momoItem.setQuantity(it.getQuantity());
			Long amountItem = it.getQuantity() * (variant.getPrice() - variant.getPrice() * variant.getDiscount() / 100);
			momoItem.setTotalPrice(amountItem);
			amount += amountItem;
			items.add(momoItem);
		}
		
		String redirectUrl = "http://localhost:5173/";
		String ipnUrl = "https://abc123.ngrok-free.app/api/pay/momo/ipn";
		String orderInfo = "Thanh toan momo";
		
		String rawSignature =
			    "accessKey=" + accessKey +
			    "&amount=" + amount +
			    "&extraData=" +
			    "&ipnUrl=" + ipnUrl +
			    "&orderId=" + orderId +
			    "&orderInfo=" + orderInfo +
			    "&partnerCode=" + partnerCode +
			    "&redirectUrl=" + redirectUrl +
			    "&requestId=" + requestId +
			    "&requestType=captureWallet";
		



        String signature = HmacUtil.hmacSHA256(rawSignature, secretKey);


        Map<String, Object> body = new HashMap<>();
        body.put("partnerCode", partnerCode);
        body.put("accessKey", accessKey);
        body.put("requestId", requestId);
        body.put("orderId", orderId);
        body.put("amount", amount);
        body.put("lang", "vi");
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", redirectUrl);
        body.put("ipnUrl", ipnUrl);
        body.put("requestType", "captureWallet");
        body.put("extraData", "");
        body.put("items", items);
        body.put("signature", signature);


        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(apiEndpoint, entity, Map.class);
        return response.getBody();
		
	}
	
	
}
