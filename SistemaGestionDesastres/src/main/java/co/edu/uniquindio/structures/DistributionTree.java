package co.edu.uniquindio.structures;

import co.edu.uniquindio.models.Resource;

/**
 * Implementación propia de un Árbol para distribución jerárquica de recursos
 * Permite organizar las ubicaciones en una jerarquía de distribución
 * Cada nodo representa una ubicación con recursos asignados
 */
public class DistributionTree {
    private TreeNode root;

    /**
     * Clase interna para representar nodos del árbol
     */
    private static class TreeNode {
        String locationId;
        CustomList<Resource> resources;
        CustomList<TreeNode> children;
        TreeNode parent;
        int depth;

        TreeNode(String locationId) {
            this.locationId = locationId;
            this.resources = new CustomList<>();
            this.children = new CustomList<>();
            this.parent = null;
            this.depth = 0;
        }
    }

    /**
     * Constructor: Crea un árbol con una ubicación raíz
     *
     * @param rootLocationId ID de la ubicación raíz
     */
    public DistributionTree(String rootLocationId) {
        if (rootLocationId == null || rootLocationId.isEmpty()) {
            throw new IllegalArgumentException("Root location ID cannot be null or empty");
        }
        this.root = new TreeNode(rootLocationId);
    }

    /**
     * Agregar una ubicación hija a un padre específico
     * Complejidad: O(n) donde n es el número de nodos
     *
     * @param parentLocationId ID de la ubicación padre
     * @param childLocationId ID de la ubicación hija
     * @return true si se agregó exitosamente, false si el padre no existe
     */
    public boolean addChild(String parentLocationId, String childLocationId) {
        if (childLocationId == null || childLocationId.isEmpty()) {
            throw new IllegalArgumentException("Child location ID cannot be null or empty");
        }

        TreeNode parent = findNode(root, parentLocationId);
        if (parent != null) {
            // Verificar que el hijo no exista ya
            if (findNode(root, childLocationId) != null) {
                return false; // Ya existe
            }

            TreeNode child = new TreeNode(childLocationId);
            child.parent = parent;
            child.depth = parent.depth + 1;
            parent.children.add(child);
            return true;
        }
        return false;
    }

    /**
     * Remover una ubicación y todos sus descendientes
     * Complejidad: O(n)
     */
    public boolean removeLocation(String locationId) {
        if (locationId.equals(root.locationId)) {
            return false; // No se puede remover la raíz
        }

        TreeNode node = findNode(root, locationId);
        if (node != null && node.parent != null) {
            node.parent.children.remove(node);
            return true;
        }
        return false;
    }

    /**
     * Asignar recursos a una ubicación
     * Complejidad: O(n) para buscar + O(1) para agregar
     *
     * @param locationId ID de la ubicación
     * @param resource Recurso a asignar
     */
    public boolean assignResources(String locationId, Resource resource) {
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null");
        }

        TreeNode node = findNode(root, locationId);
        if (node != null) {
            node.resources.add(resource);
            return true;
        }
        return false;
    }

    /**
     * Asignar múltiples recursos a una ubicación
     * Complejidad: O(n + m) donde m es el número de recursos
     */
    public boolean assignMultipleResources(String locationId, CustomList<Resource> resources) {
        TreeNode node = findNode(root, locationId);
        if (node != null) {
            for (int i = 0; i < resources.size(); i++) {
                node.resources.add(resources.get(i));
            }
            return true;
        }
        return false;
    }

    /**
     * Remover un recurso específico de una ubicación
     * Complejidad: O(n + r) donde r es el número de recursos
     */
    public boolean removeResource(String locationId, String resourceId) {
        TreeNode node = findNode(root, locationId);
        if (node != null) {
            for (int i = 0; i < node.resources.size(); i++) {
                if (node.resources.get(i).getId().equals(resourceId)) {
                    node.resources.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Obtener recursos de una ubicación
     * Complejidad: O(n)
     *
     * @return Lista de recursos, o lista vacía si no existe
     */
    public CustomList<Resource> getResources(String locationId) {
        TreeNode node = findNode(root, locationId);
        if (node != null) {
            return node.resources;
        }
        return new CustomList<>();
    }

    /**
     * Obtener todos los recursos del árbol (incluyendo descendientes)
     * Complejidad: O(n * r) donde r es el promedio de recursos por nodo
     */
    public CustomList<Resource> getAllResources(String locationId) {
        TreeNode node = findNode(root, locationId);
        if (node == null) {
            return new CustomList<>();
        }

        CustomList<Resource> allResources = new CustomList<>();
        collectAllResources(node, allResources);
        return allResources;
    }

    /**
     * Método auxiliar para recolectar recursos recursivamente
     */
    private void collectAllResources(TreeNode node, CustomList<Resource> resources) {
        if (node == null) {
            return;
        }

        // Agregar recursos del nodo actual
        for (int i = 0; i < node.resources.size(); i++) {
            resources.add(node.resources.get(i));
        }

        // Recursión en hijos
        for (int i = 0; i < node.children.size(); i++) {
            collectAllResources(node.children.get(i), resources);
        }
    }

    /**
     * Obtener todos los IDs de ubicaciones hijas directas
     * Complejidad: O(n)
     */
    public CustomList<String> getChildren(String locationId) {
        TreeNode node = findNode(root, locationId);
        CustomList<String> childIds = new CustomList<>();

        if (node != null) {
            for (int i = 0; i < node.children.size(); i++) {
                childIds.add(node.children.get(i).locationId);
            }
        }

        return childIds;
    }

    /**
     * Obtener todos los descendientes de una ubicación
     * Complejidad: O(n)
     */
    public CustomList<String> getAllDescendants(String locationId) {
        TreeNode node = findNode(root, locationId);
        CustomList<String> descendants = new CustomList<>();

        if (node != null) {
            collectDescendants(node, descendants);
        }

        return descendants;
    }

    /**
     * Método auxiliar para recolectar descendientes recursivamente
     */
    private void collectDescendants(TreeNode node, CustomList<String> descendants) {
        for (int i = 0; i < node.children.size(); i++) {
            TreeNode child = node.children.get(i);
            descendants.add(child.locationId);
            collectDescendants(child, descendants);
        }
    }

    /**
     * Obtener el padre de una ubicación
     * Complejidad: O(n)
     */
    public String getParent(String locationId) {
        TreeNode node = findNode(root, locationId);
        if (node != null && node.parent != null) {
            return node.parent.locationId;
        }
        return null;
    }

    /**
     * Buscar un nodo en el árbol
     * Complejidad: O(n)
     */
    private TreeNode findNode(TreeNode current, String locationId) {
        if (current == null) {
            return null;
        }

        if (current.locationId.equals(locationId)) {
            return current;
        }

        // Búsqueda en profundidad
        for (int i = 0; i < current.children.size(); i++) {
            TreeNode found = findNode(current.children.get(i), locationId);
            if (found != null) {
                return found;
            }
        }

        return null;
    }

    /**
     * Obtener el nivel/profundidad de una ubicación en el árbol
     * Complejidad: O(n)
     *
     * @return El nivel (0 para raíz), o -1 si no existe
     */
    public int getLevel(String locationId) {
        TreeNode node = findNode(root, locationId);
        return node != null ? node.depth : -1;
    }

    /**
     * Obtener la altura del árbol desde un nodo
     * Complejidad: O(n)
     */
    public int getHeight(String locationId) {
        TreeNode node = findNode(root, locationId);
        return node != null ? calculateHeight(node) : -1;
    }

    /**
     * Calcular altura recursivamente
     */
    private int calculateHeight(TreeNode node) {
        if (node == null || node.children.isEmpty()) {
            return 0;
        }

        int maxHeight = 0;
        for (int i = 0; i < node.children.size(); i++) {
            int childHeight = calculateHeight(node.children.get(i));
            maxHeight = Math.max(maxHeight, childHeight);
        }

        return maxHeight + 1;
    }

    /**
     * Obtener todas las ubicaciones en el árbol
     * Complejidad: O(n)
     */
    public CustomList<String> getAllLocations() {
        CustomList<String> locations = new CustomList<>();
        collectLocations(root, locations);
        return locations;
    }

    /**
     * Método auxiliar para recolectar ubicaciones recursivamente
     */
    private void collectLocations(TreeNode node, CustomList<String> locations) {
        if (node == null) {
            return;
        }

        locations.add(node.locationId);

        for (int i = 0; i < node.children.size(); i++) {
            collectLocations(node.children.get(i), locations);
        }
    }

    /**
     * Obtener la raíz del árbol
     * Complejidad: O(1)
     */
    public String getRootLocationId() {
        return root.locationId;
    }

    /**
     * Verificar si una ubicación existe en el árbol
     * Complejidad: O(n)
     */
    public boolean contains(String locationId) {
        return findNode(root, locationId) != null;
    }

    /**
     * Obtener el número de nodos en el árbol
     * Complejidad: O(n)
     */
    public int size() {
        return countNodes(root);
    }

    /**
     * Contar nodos recursivamente
     */
    private int countNodes(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int count = 1;
        for (int i = 0; i < node.children.size(); i++) {
            count += countNodes(node.children.get(i));
        }

        return count;
    }

    /**
     * Verificar si el árbol está vacío
     * Complejidad: O(1)
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Obtener si un nodo es hoja (no tiene hijos)
     * Complejidad: O(n)
     */
    public boolean isLeaf(String locationId) {
        TreeNode node = findNode(root, locationId);
        return node != null && node.children.isEmpty();
    }

    /**
     * Obtener representación en String del árbol
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Distribution Tree:\n");
        printTree(root, "", true, sb);
        return sb.toString();
    }

    /**
     * Método auxiliar para imprimir el árbol de forma visual
     */
    private void printTree(TreeNode node, String prefix, boolean isTail, StringBuilder sb) {
        if (node == null) {
            return;
        }

        sb.append(prefix).append(isTail ? "└── " : "├── ")
                .append(node.locationId)
                .append(" [").append(node.resources.size()).append(" recursos]")
                .append("\n");

        for (int i = 0; i < node.children.size(); i++) {
            printTree(
                    node.children.get(i),
                    prefix + (isTail ? "    " : "│   "),
                    i == node.children.size() - 1,
                    sb
            );
        }
    }
}
