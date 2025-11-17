package co.edu.uniquindio.models;

/**
 * Modelo para representar recursos de emergencia
 * Incluye diferentes tipos de recursos necesarios en situaciones de desastre
 */
public class Resource {
    private String id;
    private String name;
    private ResourceType type;
    private int quantity;
    private String unit;
    private int minimumStock;
    private String supplier;
    private boolean isPerishable;
    private String expirationDate;

    /**
     * Enum para los tipos de recursos disponibles
     */
    public enum ResourceType {
        FOOD("Alimentos"),
        MEDICINE("Medicinas"),
        WATER("Agua"),
        SHELTER_SUPPLIES("Suministros de Refugio"),
        RESCUE_EQUIPMENT("Equipo de Rescate"),
        MEDICAL_EQUIPMENT("Equipo Médico"),
        CLOTHING("Ropa y Abrigo"),
        FUEL("Combustible"),
        COMMUNICATION("Comunicación"),
        TOOLS("Herramientas");

        private final String displayName;

        ResourceType(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    /**
     * Constructor principal
     *
     * @param id Identificador único del recurso
     * @param name Nombre del recurso
     * @param type Tipo de recurso
     * @param quantity Cantidad disponible
     * @param unit Unidad de medida (kg, litros, unidades, etc.)
     */
    public Resource(String id, String name, ResourceType type, int quantity, String unit) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Type cannot be null");
        }
        if (unit == null || unit.isEmpty()) {
            throw new IllegalArgumentException("Unit cannot be null or empty");
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = Math.max(0, quantity);
        this.unit = unit;
        this.minimumStock = 0;
        this.supplier = "";
        this.isPerishable = false;
        this.expirationDate = "";
    }

    /**
     * Constructor completo con todos los parámetros
     */
    public Resource(String id, String name, ResourceType type, int quantity, String unit,
                    int minimumStock, String supplier, boolean isPerishable, String expirationDate) {
        this(id, name, type, quantity, unit);
        this.minimumStock = Math.max(0, minimumStock);
        this.supplier = supplier != null ? supplier : "";
        this.isPerishable = isPerishable;
        this.expirationDate = expirationDate != null ? expirationDate : "";
    }

    // ==================== GETTERS ====================

    /**
     * Obtener el ID del recurso
     */
    public String getId() {
        return id;
    }

    /**
     * Obtener el nombre del recurso
     */
    public String getName() {
        return name;
    }

    /**
     * Obtener el tipo de recurso
     */
    public ResourceType getType() {
        return type;
    }

    /**
     * Obtener la cantidad disponible
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Obtener la unidad de medida
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Obtener el stock mínimo recomendado
     */
    public int getMinimumStock() {
        return minimumStock;
    }

    /**
     * Obtener el proveedor
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Verificar si el recurso es perecedero
     */
    public boolean isPerishable() {
        return isPerishable;
    }

    /**
     * Obtener la fecha de expiración
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    // ==================== SETTERS ====================

    /**
     * Establecer el nombre del recurso
     */
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }

    /**
     * Establecer la cantidad
     * No permite valores negativos
     */
    public void setQuantity(int quantity) {
        this.quantity = Math.max(0, quantity);
    }

    /**
     * Establecer la unidad de medida
     */
    public void setUnit(String unit) {
        if (unit != null && !unit.isEmpty()) {
            this.unit = unit;
        }
    }

    /**
     * Establecer el stock mínimo
     */
    public void setMinimumStock(int minimumStock) {
        this.minimumStock = Math.max(0, minimumStock);
    }

    /**
     * Establecer el proveedor
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier != null ? supplier : "";
    }

    /**
     * Establecer si es perecedero
     */
    public void setPerishable(boolean isPerishable) {
        this.isPerishable = isPerishable;
    }

    /**
     * Establecer fecha de expiración
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate != null ? expirationDate : "";
    }

    /**
     * Establecer el tipo de recurso
     */
    public void setType(ResourceType type) {
        if (type != null) {
            this.type = type;
        }
    }
}
