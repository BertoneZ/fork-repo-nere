package org.example.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.example.Dto.OrdenDTO;
import org.example.Dto.OrdenFiltroDTO;
import org.example.models.Cliente;
import org.example.Dto.ClienteDTO;
import org.example.models.EstadoOrden;
import org.example.models.Orden;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;


public class Logica {

    // CRUD CLIENTE
    public ClienteDTO CrearCliente(ClienteDTO clientedto)  {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();
            Cliente cliente = clientedto.toEntity(); // Convertimos el DTO a entidad
            session.persist(cliente); // Aquí Hibernate ignora el 0 y pide el ID a la DB
            session.getTransaction().commit();

            // Seteamos el ID generado por la DB de vuelta al DTO
            clientedto.setIdCliente(cliente.getIdCliente());
            return clientedto;
        }
    }
    public boolean actualizarCliente(ClienteDTO clienteDTO) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();

            Cliente cliente = clienteDTO.toEntity();
            session.merge(cliente);
            session.getTransaction().commit();
            return true;
        }

    }
    public ClienteDTO buscarClientePorId(int id) {
        try (Session session = HibernateUtil.getSession()) {
            Cliente cliente = session.get(Cliente.class, id);

            if (cliente != null) {
                return ClienteDTO.fromEntity(cliente); // Usamos el mapper estático
            }
            return null;
        }
    }
    public List<ClienteDTO> leerTodosLosClientes() {
        try (Session session = HibernateUtil.getSession()) {
            // HQL: Traemos todos los clientes de la clase Cliente
            List<Cliente> lista = session.createQuery("from Cliente where activo = true", Cliente.class).list();
            return lista.stream()
                    .map(ClienteDTO::fromEntity) // Transformamos cada uno a DTO
                    .collect(Collectors.toList());
        }
    }
    public void eliminarClienteLogico(int id) {
        try (Session session = HibernateUtil.getSession()) {
            session.beginTransaction();

            Cliente cliente = session.get(Cliente.class, id);
            if (cliente != null) {
                // En lugar de borrar, desactivamos
                cliente.setActivo(false);
                session.merge(cliente);
            }
                session.getTransaction().commit();
        }
    }

    public List<OrdenDTO> buscarConFiltros(OrdenFiltroDTO filtro) {
        // 1. Abrir la sesión igual que en tus apuntes
        try (Session session = HibernateUtil.getSession()) {

            var builder = session.getCriteriaBuilder();
            var query = builder.createQuery(Orden.class);
            var root = query.from(Orden.class);

            var predicates = new ArrayList<Predicate>();

            // Filtro por Estado (Usando tu Enum)
            if (filtro.getEstado() != null && !filtro.getEstado().isEmpty()) {
                // Comparamos el campo "estado" de la entidad con el valor del Enum
                EstadoOrden estadoEnum = EstadoOrden.valueOf(filtro.getEstado());
                predicates.add(builder.equal(root.get("estado"), estadoEnum));
            }

            // Filtro por Descripción (Aproximación LIKE)
            if (filtro.getDescripcion() != null && !filtro.getDescripcion().isEmpty()) {
                // Como en el ejemplo de tu apunte [cite: 385]
                predicates.add(builder.like(root.get("descripcion"), "%" + filtro.getDescripcion() + "%"));
            }

            // Filtro por Rango de Fechas
            if (filtro.getFechaDesde() != null && filtro.getFechaHasta() != null) {
                predicates.add(builder.between(root.get("fechaIngreso"), filtro.getFechaDesde(), filtro.getFechaHasta()));
            }

            // Aplicar los filtros y ejecutar
            query.select(root).where(predicates.toArray(new Predicate[0]));

            return session.createQuery(query).getResultList()
                .stream()
                    .map(OrdenDTO::fromEntity)
                    .toList();
        }
    }
    private void validarFiltro(OrdenFiltroDTO filtro) {
        if (filtro == null) throw new RuntimeException("Filtro nulo");

        if (filtro.getFechaDesde() != null && filtro.getFechaHasta() != null) {
            if (filtro.getFechaDesde().isAfter(filtro.getFechaHasta())) {
                throw new RuntimeException("La fecha inicial no puede ser mayor a la final");
            }
        }
    }
    private void validarEstado(String estadoRecibido) {
        if (estadoRecibido == null || estadoRecibido.isEmpty()) return;

        try {
            // Quitamos espacios ("En Proceso" -> "EnProceso") para comparar con el Enum
            String valorLimpio = estadoRecibido.replace(" ", "");
            EstadoOrden.valueOf(valorLimpio);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido. Solo se permite: Pendiente, En Proceso o Finalizada.");
        }
    }

}
