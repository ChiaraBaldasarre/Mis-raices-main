package com.tienda.service;

import com.tienda.model.Contacto;
import com.tienda.repository.ContactoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactoService {

    @Autowired
    private ContactoRepository contactoRepository;

    public Contacto guardarMensaje(Contacto contacto) {
        return contactoRepository.save(contacto);
    }
}