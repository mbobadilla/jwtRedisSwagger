package org.example.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "offers")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String zone;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    private User provider;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    // 🔹 Constructores


    public Offer(String sku, String name, String zone, BigDecimal price, User provider) {
        this.sku = sku;
        this.name = name;
        this.zone = zone;
        this.price = price;
        this.provider = provider;
        this.createdAt = Instant.now();
    }

    public Offer() {

    }


    // 🔹 Método de actualización automática
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }
}

