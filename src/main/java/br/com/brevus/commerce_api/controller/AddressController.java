package br.com.brevus.commerce_api.controller;

import br.com.brevus.commerce_api.dto.AddressRequestDTO;
import br.com.brevus.commerce_api.dto.AddressResponseDTO;
import br.com.brevus.commerce_api.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@Tag(name = "Address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Operation(summary = "Register", description = "Register a new address")
    public ResponseEntity<Void> registerAddress(@Valid @RequestBody AddressRequestDTO dto){
        addressService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List", description = "List all addresses")
    public ResponseEntity<List<AddressResponseDTO>> listAllAddress(){
        List<AddressResponseDTO> responseDTOList = addressService.listAllAddress();
        return ResponseEntity.ok().body(responseDTOList);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Operation(summary = "List by User", description = "List all addresses by user id")
    public ResponseEntity<List<AddressResponseDTO>> listAddressByUserId(@PathVariable(value = "userId") UUID userId) {
        List<AddressResponseDTO> responseDTOList = addressService.listAllAddressByUserId(userId);
        return ResponseEntity.ok().body(responseDTOList);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Operation(summary = "Update", description = "Update address")
    public ResponseEntity<Void> updateAddress(@PathVariable(value = "id") UUID id, @Valid @RequestBody AddressRequestDTO dto) {
        addressService.updateAddress(id, dto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    @Operation(summary = "Delete", description = "Delete address")
    public ResponseEntity<Void> deleteAddressById(@PathVariable(value = "id") UUID id) {
        addressService.deleteAddressById(id);
        return ResponseEntity.noContent().build();
    }
}
