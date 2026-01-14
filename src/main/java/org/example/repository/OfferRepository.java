package org.example.repository;


import org.example.model.Offer;
import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfferRepository extends JpaRepository<Offer, UUID> {

    // 🔹 Buscar ofertas por SKU exacto
    List<Offer> findBySku(String sku);

    // 🔹 Buscar ofertas por nombre (sin importar mayúsculas/minúsculas)
    List<Offer> findByNameIgnoreCase(String name);

    // 🔹 Buscar ofertas por zona
    List<Offer> findByZone(String zone);

    // 🔹 Buscar ofertas por proveedor específico
    List<Offer> findByProvider(User provider);

    // 🔹 Agrupar productos conceptualmente (sku o nombre)
    @Query("""
        SELECT o FROM Offer o
        WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :term, '%'))
        OR o.sku = :term
    """)
    List<Offer> searchOffers(String term);
}

