package co.edu.uniquindio.services;

import co.edu.uniquindio.models.*;

/**
 * Clase para cargar datos de prueba en el sistema
 */
public class DataLoader {

    /**
     * Cargar datos de prueba completos
     */
    public static void loadTestData(DisasterManager manager) {
        loadLocations(manager);
        loadRoutes(manager);
        loadResources(manager);
        loadRescueTeams(manager);
    }

    /**
     * Cargar ubicaciones de prueba
     */
    private static void loadLocations(DisasterManager manager) {
        // Ciudades afectadas
        manager.addLocation(new Location(
                "LOC001", "Centro Urbano Principal",
                Location.LocationType.CITY, 5000, 5, 4.5981, -74.0758
        ));

        manager.addLocation(new Location(
                "LOC002", "Zona Industrial Norte",
                Location.LocationType.AFFECTED_ZONE, 3000, 4, 4.6097, -74.0817
        ));

        manager.addLocation(new Location(
                "LOC003", "Barrio Residencial Sur",
                Location.LocationType.AFFECTED_ZONE, 4500, 4, 4.5709, -74.2973
        ));

        // Refugios
        manager.addLocation(new Location(
                "LOC004", "Refugio Municipal Central",
                Location.LocationType.SHELTER, 0, 2, 4.6343, -74.0672
        ));

        manager.addLocation(new Location(
                "LOC005", "Centro de Evacuación Este",
                Location.LocationType.SHELTER, 0, 2, 4.6097, -74.0672
        ));

        // Centros de ayuda
        manager.addLocation(new Location(
                "LOC006", "Centro de Ayuda Humanitaria",
                Location.LocationType.AID_CENTER, 0, 1, 4.6482, -74.1031
        ));

        manager.addLocation(new Location(
                "LOC007", "Punto de Distribución Oeste",
                Location.LocationType.AID_CENTER, 0, 1, 4.6684, -74.0564
        ));

        // Hospitales
        manager.addLocation(new Location(
                "LOC008", "Hospital General de Emergencias",
                Location.LocationType.HOSPITAL, 0, 3, 4.6533, -74.0836
        ));

        manager.addLocation(new Location(
                "LOC009", "Centro Médico Metropolitano",
                Location.LocationType.HOSPITAL, 0, 3, 4.6294, -74.0640
        ));

        // Más zonas afectadas
        manager.addLocation(new Location(
                "LOC010", "Comunidad Rural Valle Verde",
                Location.LocationType.AFFECTED_ZONE, 1500, 5, 4.7110, -74.0721
        ));
    }

    /**
     * Cargar rutas entre ubicaciones
     */
    private static void loadRoutes(DisasterManager manager) {
        // Rutas desde Centro Urbano Principal
        manager.addRoute("LOC001", "LOC002", 5.2);
        manager.addRoute("LOC001", "LOC003", 8.5);
        manager.addRoute("LOC001", "LOC004", 3.1);
        manager.addRoute("LOC001", "LOC008", 4.8);

        // Rutas desde Zona Industrial
        manager.addRoute("LOC002", "LOC005", 6.3);
        manager.addRoute("LOC002", "LOC006", 7.2);
        manager.addRoute("LOC002", "LOC010", 9.1);

        // Rutas desde Barrio Residencial
        manager.addRoute("LOC003", "LOC004", 10.5);
        manager.addRoute("LOC003", "LOC007", 12.3);
        manager.addRoute("LOC003", "LOC009", 8.9);

        // Rutas desde refugios
        manager.addRoute("LOC004", "LOC006", 4.5);
        manager.addRoute("LOC004", "LOC008", 3.2);
        manager.addRoute("LOC005", "LOC007", 5.8);
        manager.addRoute("LOC005", "LOC009", 6.1);

        // Rutas desde centros de ayuda
        manager.addRoute("LOC006", "LOC007", 8.0);
        manager.addRoute("LOC006", "LOC008", 5.5);
        manager.addRoute("LOC007", "LOC009", 4.2);

        // Rutas entre hospitales
        manager.addRoute("LOC008", "LOC009", 7.3);

        // Rutas desde comunidad rural
        manager.addRoute("LOC010", "LOC005", 11.5);
        manager.addRoute("LOC010", "LOC006", 13.2);

        // Algunas rutas bidireccionales
        manager.addRoute("LOC004", "LOC001", 3.1);
        manager.addRoute("LOC006", "LOC004", 4.5);
        manager.addRoute("LOC009", "LOC007", 4.2);
    }

    /**
     * Cargar recursos de emergencia
     */
    private static void loadResources(DisasterManager manager) {
        // Alimentos
        manager.addResource(new Resource(
                "RES001", "Raciones de Comida",
                Resource.ResourceType.FOOD, 5000, "raciones"
        ));

        manager.addResource(new Resource(
                "RES002", "Agua Potable",
                Resource.ResourceType.WATER, 10000, "litros"
        ));

        manager.addResource(new Resource(
                "RES003", "Alimentos No Perecederos",
                Resource.ResourceType.FOOD, 3000, "kg"
        ));

        // Medicinas
        manager.addResource(new Resource(
                "RES004", "Kit Médico de Emergencia",
                Resource.ResourceType.MEDICINE, 500, "kits"
        ));

        manager.addResource(new Resource(
                "RES005", "Antibióticos",
                Resource.ResourceType.MEDICINE, 1000, "dosis"
        ));

        manager.addResource(new Resource(
                "RES006", "Analgésicos",
                Resource.ResourceType.MEDICINE, 2000, "dosis"
        ));

        // Equipamiento
        manager.addResource(new Resource(
                "RES007", "Mantas Térmicas",
                Resource.ResourceType.SHELTER_SUPPLIES, 3000, "unidades"
        ));

        manager.addResource(new Resource(
                "RES008", "Tiendas de Campaña",
                Resource.ResourceType.SHELTER_SUPPLIES, 200, "unidades"
        ));

        manager.addResource(new Resource(
                "RES009", "Equipo de Rescate",
                Resource.ResourceType.RESCUE_EQUIPMENT, 150, "sets"
        ));

        manager.addResource(new Resource(
                "RES010", "Generadores Eléctricos",
                Resource.ResourceType.RESCUE_EQUIPMENT, 50, "unidades"
        ));

        // Ropa
        manager.addResource(new Resource(
                "RES011", "Ropa de Abrigo",
                Resource.ResourceType.CLOTHING, 2000, "prendas"
        ));

        // Combustible
        manager.addResource(new Resource(
                "RES012", "Combustible Diésel",
                Resource.ResourceType.FUEL, 5000, "litros"
        ));
    }

    /**
     * Cargar equipos de rescate
     */
    private static void loadRescueTeams(DisasterManager manager) {
        // Equipos médicos
        manager.addRescueTeam(new RescueTeam(
                "TEAM001", "Equipo Médico Alpha",
                RescueTeam.TeamType.MEDICAL, 8
        ));

        manager.addRescueTeam(new RescueTeam(
                "TEAM002", "Equipo Médico Beta",
                RescueTeam.TeamType.MEDICAL, 6
        ));

        manager.addRescueTeam(new RescueTeam(
                "TEAM003", "Paramédicos de Emergencia",
                RescueTeam.TeamType.PARAMEDICS, 10
        ));

        // Bomberos
        manager.addRescueTeam(new RescueTeam(
                "TEAM004", "Cuerpo de Bomberos A",
                RescueTeam.TeamType.FIREFIGHTERS, 12
        ));

        manager.addRescueTeam(new RescueTeam(
                "TEAM005", "Cuerpo de Bomberos B",
                RescueTeam.TeamType.FIREFIGHTERS, 10
        ));

        // Búsqueda y rescate
        manager.addRescueTeam(new RescueTeam(
                "TEAM006", "Equipo de Búsqueda y Rescate",
                RescueTeam.TeamType.SEARCH_AND_RESCUE, 15
        ));

        manager.addRescueTeam(new RescueTeam(
                "TEAM007", "Rescate Especializado",
                RescueTeam.TeamType.SEARCH_AND_RESCUE, 8
        ));

        // Policía
        manager.addRescueTeam(new RescueTeam(
                "TEAM008", "Unidad Policial de Emergencia",
                RescueTeam.TeamType.POLICE, 20
        ));

        // Ingenieros
        manager.addRescueTeam(new RescueTeam(
                "TEAM009", "Equipo de Ingeniería Civil",
                RescueTeam.TeamType.ENGINEERS, 7
        ));

        manager.addRescueTeam(new RescueTeam(
                "TEAM010", "Ingenieros de Infraestructura",
                RescueTeam.TeamType.ENGINEERS, 9
        ));
    }
}