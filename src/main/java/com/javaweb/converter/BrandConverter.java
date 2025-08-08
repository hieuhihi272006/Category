package com.javaweb.converter;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.javaweb.model.entity.BrandEntity;
import com.javaweb.model.response.BrandResponse;

@Component
public class BrandConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	public List<BrandResponse> toBrandResponse(List<BrandEntity> ListBrandEntity , List<Integer> listBrandId){
		List<BrandResponse> result = new ArrayList<>();
		for(BrandEntity it : ListBrandEntity) {
			BrandResponse brand = modelMapper.map(it, BrandResponse.class);
			if(listBrandId != null) {
				if(listBrandId.contains(it.getId())) {
					brand.setChecked("Checked");
				}
			}
			result.add(brand);
		}
		return result;
	}

}
