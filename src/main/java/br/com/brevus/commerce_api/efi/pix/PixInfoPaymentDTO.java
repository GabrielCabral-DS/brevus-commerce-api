package br.com.brevus.commerce_api.efi.pix;


public class PixInfoPaymentDTO {

    private String cpfPagador;
    private String nomePagador;
    private String descricaoProduto;
    private String valorProduto;


    public PixInfoPaymentDTO(){

    }

    public PixInfoPaymentDTO(String cpfPagador, String nomePagador, String descricaoProduto, String valorProduto) {
        this.cpfPagador = cpfPagador;
        this.nomePagador = nomePagador;
        this.descricaoProduto = descricaoProduto;
        this.valorProduto = valorProduto;
    }

    public String getCpfPagador() {
        return cpfPagador;
    }

    public void setCpfPagador(String cpfPagador) {
        this.cpfPagador = cpfPagador;
    }

    public String getNomePagador() {
        return nomePagador;
    }

    public void setNomePagador(String nomePagador) {
        this.nomePagador = nomePagador;
    }

    public String getDescricaoProduto() {
        return descricaoProduto;
    }

    public void setDescricaoProduto(String descricaoProduto) {
        this.descricaoProduto = descricaoProduto;
    }

    public String getValorProduto() {
        return valorProduto;
    }

    public void setValorProduto(String valorProduto) {
        this.valorProduto = valorProduto;
    }
}
