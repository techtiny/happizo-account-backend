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
    private String businessType; // B2B, B2C

    @Column(precision = 15, scale = 2)
    private BigDecimal quoteGross = BigDecimal.ZERO;

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
    public BigDecimal getQuoteGross() { return quoteGross; }
    public void setQuoteGross(BigDecimal quoteGross) { this.quoteGross = quoteGross; }
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
