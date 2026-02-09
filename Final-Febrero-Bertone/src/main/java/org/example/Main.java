package org.example;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.Dto.OrdenDTO;
import org.example.Dto.OrdenFiltroDTO;
import org.example.models.Cliente;
import org.example.service.Logica;
import org.example.util.HibernateUtil;
import org.hibernate.Session;
import org.example.Dto.ClienteDTO;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Iniciando prueba de conexión ---");
        Logica logica = new Logica();
//        // Usamos try-with-resources para que la sesión se cierre sola
        try (Session session = HibernateUtil.getSession()) {

            // HQL: "from Cliente" busca todos los registros de la tabla mapeada a la clase Cliente
            List<Cliente> clientes = session.createQuery("from Cliente", Cliente.class).list();

            if (clientes.isEmpty()) {
                System.out.println("Conexión exitosa, pero no se encontraron clientes. ¿Corriste los INSERT en Workbench?");
            } else {
                System.out.println("¡Éxito! Se encontraron " + clientes.size() + " clientes:");
                for (Cliente c : clientes) {
                    // Asegurate de tener los getters en tu clase Cliente
                    System.out.println("ID: " + c.getIdCliente() + " | Nombre: " + c.getNombre() + " " + c.getApellido());
                }
            }

        } catch (Exception e) {
            System.err.println("--- ERROR AL CONECTAR CON DOCKER ---");
            System.err.println("Mensaje: " + e.getMessage());
            e.printStackTrace();
        }


            // 1. PROBAR CREAR
            System.out.println("--- Probando Crear Cliente ---");
            ClienteDTO nuevo = new ClienteDTO();
            nuevo.setNombre("fede");
            nuevo.setApellido("Bertone");
            nuevo.setTelefono("123789");
            nuevo.setEmail("zoe@test.com");
            nuevo.setVehiculo("Fiat Cronos");
            nuevo.setActivo(true); // Importante para que aparezca en las listas

            nuevo = logica.CrearCliente(nuevo);
            System.out.println("Cliente creado con ID: " + nuevo.getIdCliente());

        // 2. PROBAR LEER UNO Y ACTUALIZAR
        System.out.println("\n--- Probando Actualizar ---");
        ClienteDTO aEditar = logica.buscarClientePorId(nuevo.getIdCliente());
        if (aEditar != null) {
            aEditar.setVehiculo("Tesla Model 3"); // Cambiamos el auto
            logica.actualizarCliente(aEditar);
            System.out.println("Vehículo actualizado con éxito.");
        }

        // 3. PROBAR LEER TODOS
        System.out.println("\n--- Lista de Clientes Activos ---");
        List<ClienteDTO> todos = logica.leerTodosLosClientes();
        for (ClienteDTO c : todos) {
            System.out.println(c.getIdCliente() + " | " + c.getNombre() + " | " + c.getVehiculo());
        }

        // 4. PROBAR ELIMINADO LÓGICO
        System.out.println("\n--- Probando Borrado Lógico ---");
        logica.eliminarClienteLogico(nuevo.getIdCliente());

        // Verificamos si sigue apareciendo
        List<ClienteDTO> listaPostBorrado = logica.leerTodosLosClientes();
        System.out.println("Cantidad de clientes activos ahora: " + listaPostBorrado.size());



       // 2. Configurar el filtro de prueba (como pide el punto 55 del examen)
        // Filtrar órdenes "Pendiente" entre el 15 y 20 de diciembre de 2024
        OrdenFiltroDTO filtro = new OrdenFiltroDTO();
        // El String debe ser exacto al nombre en el Enum (ej: "PENDIENTE")
        filtro.setEstado("EnProceso"); // Asegúrate de que este estado exista en tu Enum EstadoOrden
//
//
//
//        // Usamos LocalDateTime como definiste en tu OrdenFiltroDTO
        filtro.setFechaDesde(LocalDateTime.of(2024, 12, 1, 0, 0));
        filtro.setFechaHasta(LocalDateTime.of(2024, 12, 31, 23, 59));

        System.out.println("--- Iniciando búsqueda de órdenes ---");

        try {
            // 3. Ejecutar la búsqueda
           List<OrdenDTO> resultados = logica.buscarConFiltros(filtro);

           // 4. Mostrar resultados
            if (resultados.isEmpty()) {
                System.out.println("No se encontraron órdenes con esos filtros.");
            } else {
                for (OrdenDTO dto : resultados) {
                    System.out.println("ID Orden: " + dto.getIdOrden());
                    System.out.println("Fecha Ingreso: " + dto.getFechaIngreso());// Usando tus getters
                    System.out.println("Descripción: " + dto.getDescripcionProblema());
                    System.out.println("Estado: " + dto.getEstado());

                    System.out.println("-------------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al filtrar: " + e.getMessage());
            e.printStackTrace();
        }

    }
}

