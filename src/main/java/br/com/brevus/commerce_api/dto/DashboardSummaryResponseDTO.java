package br.com.brevus.commerce_api.dto;

import java.math.BigDecimal;

public record DashboardSummaryResponseDTO(

        BigDecimal totalRevenue,
        long totalProducts,
        long totalSales,
        long totalCustomers
) {
}
