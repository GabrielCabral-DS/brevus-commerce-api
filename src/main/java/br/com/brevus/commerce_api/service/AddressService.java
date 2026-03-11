package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.AddressRequestDTO;
import br.com.brevus.commerce_api.dto.AddressResponseDTO;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.AddressMapper;
import br.com.brevus.commerce_api.model.Address;
import br.com.brevus.commerce_api.model.User;
import br.com.brevus.commerce_api.repository.AddressRepository;
import br.com.brevus.commerce_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;


    public AddressService(AddressRepository addressRepository, UserRepository userRepository, AddressMapper addressMapper) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.addressMapper = addressMapper;
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
}
