package com.viafluvial.srvusuario.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Documentos do barqueiro")
public class BoatmanDocumentsDTO {

    @Schema(description = "URL do documento de CPF", example = "https://.../cpf.pdf")
    private String documentCpfUrl;

    @Schema(description = "URL do documento de CNPJ", example = "https://.../cnpj.pdf")
    private String documentCnpjUrl;

    @Schema(description = "URL do comprovante de endereço", example = "https://.../endereco.pdf")
    private String documentAddressProofUrl;

    public BoatmanDocumentsDTO() {
    }

    public BoatmanDocumentsDTO(String documentCpfUrl, String documentCnpjUrl, String documentAddressProofUrl) {
        this.documentCpfUrl = documentCpfUrl;
        this.documentCnpjUrl = documentCnpjUrl;
        this.documentAddressProofUrl = documentAddressProofUrl;
    }

    public String getDocumentCpfUrl() {
        return documentCpfUrl;
    }

    public void setDocumentCpfUrl(String documentCpfUrl) {
        this.documentCpfUrl = documentCpfUrl;
    }

    public String getDocumentCnpjUrl() {
        return documentCnpjUrl;
    }

    public void setDocumentCnpjUrl(String documentCnpjUrl) {
        this.documentCnpjUrl = documentCnpjUrl;
    }

    public String getDocumentAddressProofUrl() {
        return documentAddressProofUrl;
    }

    public void setDocumentAddressProofUrl(String documentAddressProofUrl) {
        this.documentAddressProofUrl = documentAddressProofUrl;
    }

    public static BoatmanDocumentsDTOBuilder builder() {
        return new BoatmanDocumentsDTOBuilder();
    }

    public static class BoatmanDocumentsDTOBuilder {
        private String documentCpfUrl;
        private String documentCnpjUrl;
        private String documentAddressProofUrl;

        public BoatmanDocumentsDTOBuilder documentCpfUrl(String documentCpfUrl) {
            this.documentCpfUrl = documentCpfUrl;
            return this;
        }

        public BoatmanDocumentsDTOBuilder documentCnpjUrl(String documentCnpjUrl) {
            this.documentCnpjUrl = documentCnpjUrl;
            return this;
        }

        public BoatmanDocumentsDTOBuilder documentAddressProofUrl(String documentAddressProofUrl) {
            this.documentAddressProofUrl = documentAddressProofUrl;
            return this;
        }

        public BoatmanDocumentsDTO build() {
            return new BoatmanDocumentsDTO(documentCpfUrl, documentCnpjUrl, documentAddressProofUrl);
        }
    }
}
