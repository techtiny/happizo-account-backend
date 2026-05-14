package com.happizo.account.dto;

import java.math.BigDecimal;

public class ProjectDto {

    private Long id;
    private String name;
    private String client;
    private String fy;
    private String status;
    private String businessType;
    private String location;
    private String description;
    private String clientGstNo;
    private String clientAddress;
    private String billingAddress;
    private String poWoStatus;
    private String orderType;
    private String orderNumber;
    private Integer gstPct;
    private Integer quoteGstPct;
    private BigDecimal quoteGross;
    private BigDecimal quoteValue;
    private BigDecimal totalValue;
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
            this.material = material; this.labour = labour; this.subcontract = subcontract;
            this.consultants = consultants; this.miscellaneous = miscellaneous;
        }
        public BigDecimal getMaterial() { return material; }
        public void setMaterial(BigDecimal m) { this.material = m; }
        public BigDecimal getLabour() { return labour; }
        public void setLabour(BigDecimal l) { this.labour = l; }
        public BigDecimal getSubcontract() { return subcontract; }
        public void setSubcontract(BigDecimal s) { this.subcontract = s; }
        public BigDecimal getConsultants() { return consultants; }
        public void setConsultants(BigDecimal c) { this.consultants = c; }
        public BigDecimal getMiscellaneous() { return miscellaneous; }
        public void setMiscellaneous(BigDecimal m) { this.miscellaneous = m; }
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
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getClientGstNo() { return clientGstNo; }
    public void setClientGstNo(String clientGstNo) { this.clientGstNo = clientGstNo; }
    public String getClientAddress() { return clientAddress; }
    public void setClientAddress(String clientAddress) { this.clientAddress = clientAddress; }
    public String getBillingAddress() { return billingAddress; }
    public void setBillingAddress(String billingAddress) { this.billingAddress = billingAddress; }
    public String getPoWoStatus() { return poWoStatus; }
    public void setPoWoStatus(String poWoStatus) { this.poWoStatus = poWoStatus; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Integer getGstPct() { return gstPct; }
    public void setGstPct(Integer gstPct) { this.gstPct = gstPct; }
    public Integer getQuoteGstPct() { return quoteGstPct; }
    public void setQuoteGstPct(Integer quoteGstPct) { this.quoteGstPct = quoteGstPct; }
    public BigDecimal getQuoteGross() { return quoteGross; }
    public void setQuoteGross(BigDecimal quoteGross) { this.quoteGross = quoteGross; }
    public BigDecimal getQuoteValue() { return quoteValue; }
    public void setQuoteValue(BigDecimal quoteValue) { this.quoteValue = quoteValue; }
    public BigDecimal getTotalValue() { return totalValue; }
    public void setTotalValue(BigDecimal totalValue) { this.totalValue = totalValue; }
    public BigDecimal getCollectionReceived() { return collectionReceived; }
    public void setCollectionReceived(BigDecimal collectionReceived) { this.collectionReceived = collectionReceived; }
    public Expenses getExpenses() { return expenses; }
    public void setExpenses(Expenses expenses) { this.expenses = expenses; }
}
