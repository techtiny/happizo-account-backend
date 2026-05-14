package com.happizo.account.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "projects", uniqueConstraints = @jakarta.persistence.UniqueConstraint(columnNames = "name"))
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String client;
    private String fy;
    private String status;
    private String businessType;

    // PWJ-aligned fields
    private String location;
    private String description;
    private String clientGstNo;
    private String clientAddress;
    private String billingAddress;
    private String poWoStatus;
    private String orderType;   // PO, WO, JO

    @Column(unique = true)
    private String orderNumber; // e.g. PO-2526-001 (auto-generated)

    private Integer gstPct;
    private Integer quoteGstPct;

    @Column(precision = 15, scale = 2)
    private BigDecimal quoteGross = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal quoteValue;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalValue;

    @Column(precision = 15, scale = 2)
    private BigDecimal collectionReceived = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal expMaterial = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal expLabour = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal expSubcontract = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal expConsultants = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal expMiscellaneous = BigDecimal.ZERO;

    public Project() {}

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
    public BigDecimal getExpMaterial() { return expMaterial; }
    public void setExpMaterial(BigDecimal expMaterial) { this.expMaterial = expMaterial; }
    public BigDecimal getExpLabour() { return expLabour; }
    public void setExpLabour(BigDecimal expLabour) { this.expLabour = expLabour; }
    public BigDecimal getExpSubcontract() { return expSubcontract; }
    public void setExpSubcontract(BigDecimal expSubcontract) { this.expSubcontract = expSubcontract; }
    public BigDecimal getExpConsultants() { return expConsultants; }
    public void setExpConsultants(BigDecimal expConsultants) { this.expConsultants = expConsultants; }
    public BigDecimal getExpMiscellaneous() { return expMiscellaneous; }
    public void setExpMiscellaneous(BigDecimal expMiscellaneous) { this.expMiscellaneous = expMiscellaneous; }
}
