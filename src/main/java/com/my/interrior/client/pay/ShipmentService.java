package com.my.interrior.client.pay;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShipmentService {

	private final ShipmentRepository shipmentRepository;
	
	public void saveMyShipment(ShipmentEntity ship) throws Exception{
		
		shipmentRepository.save(ship);
	}
}
