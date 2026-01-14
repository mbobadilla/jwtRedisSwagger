package org.example.controller;

import org.example.model.Offer;
import org.example.model.User;
import org.example.repository.OfferRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class OfferController {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private UserRepository userRepository;

    // 🔹 Crear nueva oferta
    @PostMapping
    public ResponseEntity<?> createOffer(@RequestBody Map<String, Object> data) {
        try {
            String providerId = (String) data.get("providerId");
            Optional<User> providerOpt = userRepository.findById(UUID.fromString(providerId));
            if (providerOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Proveedor no encontrado.");
            }

            Offer offer = new Offer();
            offer.setSku((String) data.get("sku"));
            offer.setName((String) data.get("name"));
            offer.setZone((String) data.get("zone"));
            offer.setPrice(new java.math.BigDecimal(data.get("price").toString()));
            offer.setProvider(providerOpt.get());

            Offer saved = offerRepository.save(offer);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creando la oferta: " + e.getMessage());
        }
    }

    // 🔹 Listar todas las ofertas
    @GetMapping
    public List<Offer> listOffers() {
        return offerRepository.findAll();
    }

    // 🔹 Buscar por SKU o nombre (agrupado)
    @GetMapping("/search")
    public List<Offer> searchOffers(@RequestParam("q") String term) {
        return offerRepository.searchOffers(term);
    }

    // 🔹 Filtrar por zona
    @GetMapping("/zone/{zone}")
    public List<Offer> getByZone(@PathVariable String zone) {
        return offerRepository.findByZone(zone);
    }

    // 🔹 Listar ofertas de un proveedor específico
    @GetMapping("/provider/{id}")
    public ResponseEntity<?> getByProvider(@PathVariable UUID id) {
        Optional<User> provider = userRepository.findById(id);
        if (provider.isEmpty()) {
            return ResponseEntity.badRequest().body("Proveedor no encontrado.");
        }
        return ResponseEntity.ok(offerRepository.findByProvider(provider.get()));
    }
}

