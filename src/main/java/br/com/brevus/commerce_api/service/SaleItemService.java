package br.com.brevus.commerce_api.service;

import br.com.brevus.commerce_api.dto.SaleItemResponseDTO;
import br.com.brevus.commerce_api.mapper.SaleItemMapper;
import br.com.brevus.commerce_api.model.SaleItem;
import br.com.brevus.commerce_api.repository.ProductRepository;
import br.com.brevus.commerce_api.repository.SaleItemRepository;
import br.com.brevus.commerce_api.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleItemService {

    private final SaleItemRepository saleItemRepository;
    private final SaleItemMapper saleItemMapper;
    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;


    public SaleItemService(SaleItemRepository saleItemRepository, SaleItemMapper saleItemMapper, SaleRepository saleRepository, ProductRepository productRepository) {
        this.saleItemRepository = saleItemRepository;
        this.saleItemMapper = saleItemMapper;
        this.saleRepository = saleRepository;
        this.productRepository = productRepository;
    }

    public List<SaleItemResponseDTO> listALLSaleItems(){
        List<SaleItem> saleItemList = saleItemRepository.findAll();
        return saleItemMapper.toDtoList(saleItemList);
    }
}
