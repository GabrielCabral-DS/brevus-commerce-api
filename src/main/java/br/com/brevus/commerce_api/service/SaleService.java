package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.SaleItemRequestDTO;
import br.com.brevus.commerce_api.dto.SaleRequestDTO;
import br.com.brevus.commerce_api.dto.SaleResponseDTO;
import br.com.brevus.commerce_api.enums.DeliveryStatus;
import br.com.brevus.commerce_api.enums.SaleStatus;
import br.com.brevus.commerce_api.exceptions.ResourceNotFoundException;
import br.com.brevus.commerce_api.mapper.SaleMapper;
import br.com.brevus.commerce_api.model.*;
import br.com.brevus.commerce_api.repository.AddressRepository;
import br.com.brevus.commerce_api.repository.ProductRepository;
import br.com.brevus.commerce_api.repository.SaleRepository;
import br.com.brevus.commerce_api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final SaleMapper saleMapper;
    private final ProductRepository productRepository;


    public SaleService(SaleRepository saleRepository, UserRepository userRepository, AddressRepository addressRepository, SaleMapper saleMapper, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.saleMapper = saleMapper;
        this.productRepository = productRepository;
    }


    private String generateDeliveryCode() {
        int code = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }

    @Transactional
    public Sale register(SaleRequestDTO dto){

        Sale sale = new Sale();

        User userClient = userRepository.findById(dto.clientId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        User userSeller = userRepository.findById(dto.sellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestador não encontrado"));

        Address address = addressRepository.findById(dto.addressId())
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));

        String deliveryCode = generateDeliveryCode();

        sale.setClient(userClient);
        sale.setSeller(userSeller);
        sale.setDeliveryCode(deliveryCode);
        sale.setDeliveryAddress(address);
        sale.setStatus(SaleStatus.PENDING);
        sale.setDeliveryStatus(DeliveryStatus.AGUARDANDO_PROCESSAMENTO);

        List<SaleItem> items = new ArrayList<>();

        for (SaleItemRequestDTO itemDTO : dto.items()) {

            Product product = productRepository.findById(itemDTO.productId())
                    .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

            SaleItem item = new SaleItem();

            item.setSale(sale);
            item.setProduct(product);
            item.setQuantity(itemDTO.quantity());
            item.setUnitPrice(product.getPrice());

            BigDecimal subtotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDTO.quantity()));

            item.setSubtotal(subtotal);

            items.add(item);
        }

        sale.setItems(items);

        BigDecimal total = items.stream()
                .map(SaleItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        sale.setTotalAmount(total);

        return saleRepository.save(sale);
    }

    @Transactional
    public void confirmDelivery(UUID saleId, String code){

        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada"));

        if(!sale.getDeliveryCode().equals(code)){
            throw new RuntimeException("Código de entrega inválido");
        }

        sale.setDeliveryStatus(DeliveryStatus.ENTREGUE);
        sale.setDeliveryCodeConfirmedAt(LocalDateTime.now());
    }

    public List<SaleResponseDTO> listAllSales(){
        List<Sale> saleResponseDTOList = saleRepository.findAllComplete();
        return saleMapper.toDtoList(saleResponseDTOList);
    }


}
