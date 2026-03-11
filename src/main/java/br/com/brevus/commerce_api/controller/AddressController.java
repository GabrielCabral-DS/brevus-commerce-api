package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.AddressRequestDTO;
import br.com.brevus.commerce_api.dto.AddressResponseDTO;
import br.com.brevus.commerce_api.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Void> registerAddress(@Valid @RequestBody AddressRequestDTO dto){
        addressService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AddressResponseDTO>> listAllAddress(){
        List<AddressResponseDTO> responseDTOList = addressService.listAllAddress();
        return ResponseEntity.ok().body(responseDTOList);
    }

}
