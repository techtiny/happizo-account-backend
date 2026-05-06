package com.happizo.account.dto;

import java.math.BigDecimal;

public class ProjectDto {

    private Long id;
    private String name;
    private String client;
    private String fy;
    private String status;
    private String businessType;
    private BigDecimal quoteGross;
    private BigDecimal collectionReceived;
    private Expenses expenses;

    public ProjectDto() {}

    public static class Expenses {
        private BigDecimal material;
        private BigDecimal labour;
        private BigDecimal subcontract;
        private BigDecimal consultants;
        private BigDecimal miscellaneous;

        public Expenses() {}
        public Expenses(BigDecimal material, BigDecimal labour, BigDecimal subcontract,
                        BigDecimal consultants, BigDecimal miscellaneous) {
            this.material = material;
            this.labour = labour;
            this.subcontract = subcontract;
            this.consultants = consultants;
            this.miscellaneous = miscellaneous;
        }

        public BigDecimal getMaterial() { return material; }
        public void setMaterial(BigDecimal material) { this.material = material; }
        public BigDecimal getLabour() { return labour; }
        public void setLabour(BigDecimal labour) { this.labour = labour; }
        public BigDecimal getSubcontract() { return subcontract; }
        public void setSubcontract(BigDecimal subcontract) { this.subcontract = subcontract; }
        public BigDecimal getConsultants() { return consultants; }
        public void setConsultants(BigDecimal consultants) { this.consultants = consultants; }
        public BigDecimal getMiscellaneous() { return miscellaneous; }
        public void setMiscellaneous(BigDecimal miscellaneous) { this.miscellaneous = miscellaneous; }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getClient() { return client; }
    public void setClient(String client) { this.client = client; }
    public String getFy() { return fy; }
    public void setFy(String fy) { this.fy = fy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public BigDecimal getQuoteGross() { return quoteGross; }
    public void setQuoteGross(BigDecimal quoteGross) { this.quoteGross = quoteGross; }
    public BigDecimal getCollectionReceived() { return collectionReceived; }
    public void setCollectionReceived(BigDecimal collectionReceived) { this.collectionReceived = collectionReceived; }
    public Expenses getExpenses() { return expenses; }
    public void setExpenses(Expenses expenses) { this.expenses = expenses; }
}
