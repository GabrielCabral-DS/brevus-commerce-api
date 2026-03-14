package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.AddressRequestDTO;
import br.com.brevus.commerce_api.dto.AddressResponseDTO;
import br.com.brevus.commerce_api.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{userId}")
    public ResponseEntity<List<AddressResponseDTO>> listAddressByUserId(@PathVariable(value = "userId") UUID userId) {
        List<AddressResponseDTO> responseDTOList = addressService.listAllAddressByUserId(userId);
        return ResponseEntity.ok().body(responseDTOList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAddress(@PathVariable(value = "id") UUID id, @Valid @RequestBody AddressRequestDTO dto) {
        addressService.updateAddress(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable(value = "id") UUID id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }
}
