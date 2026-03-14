package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.AddressRequestDTO;
import br.com.brevus.commerce_api.dto.AddressResponseDTO;
import br.com.brevus.commerce_api.exceptions.BusinessException;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.AddressMapper;
import br.com.brevus.commerce_api.model.Address;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.AddressRepository;
import br.com.brevus.commerce_api.repository.SaleRepository;
import br.com.brevus.commerce_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;
    private final SaleRepository saleRepository;


    public AddressService(AddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper, SaleRepository saleRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
        this.saleRepository = saleRepository;
    }

    public Address register(AddressRequestDTO dto){
        Address address = addressMapper.toEntity(dto);

        User userClient =  userRepository.findById(dto.userId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        address.setUser(userClient);
        return addressRepository.save(address);

    }

    public List<AddressResponseDTO> listAllAddress(){
        List<Address> addressList = addressRepository.findAll();
        return addressMapper.toDtoList(addressList);
    }

    public List<AddressResponseDTO> listAllAddressByUserId(UUID id){
        List<Address> address = addressRepository.findByUserId(id);
        return addressMapper.toDtoList(address);
    }

    public Address updateAddress(UUID id, AddressRequestDTO dto){
        Address address = addressRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Address not found for id"));

        User userClient =  userRepository.findById(dto.userId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        address.setStreet(dto.street());
        address.setNumber(dto.number());
        address.setComplement(dto.complement());
        address.setNeighborhood(dto.neighborhood());
        address.setCity(dto.city());
        address.setState(dto.state());
        address.setZipCode(dto.zipCode());
        address.setUser(userClient);

        return addressRepository.save(address);
    }


    public void deleteAddressById(UUID id){

        if (saleRepository.existsByDeliveryAddressId(id)) {
            throw new BusinessException("Cannot delete address because it is associated with a sale.");
        }

        Address address = addressRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Address not found for id"));
        addressRepository.delete(address);
    }
}
