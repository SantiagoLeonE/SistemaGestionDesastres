package co.edu.uniquindio.structures;

import java.util.Comparator;

/**
 * Implementación propia de una Cola de Prioridad usando Min-Heap
 * Similar a PriorityQueue de Java, pero implementada desde cero
 * Utiliza un heap binario para mantener el orden de prioridad
 */
public class PriorityQueue<T> {
    private Object[] heap;
    private int size;
    private Comparator<T> comparator;
    private static final int INITIAL_CAPACITY = 11;

    /**
     * Constructor con comparador personalizado
     * @param comparator Define el orden de prioridad
     */
    public PriorityQueue(Comparator<T> comparator) {
        this.heap = new Object[INITIAL_CAPACITY];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Constructor con capacidad inicial y comparador
     */
    public PriorityQueue(int initialCapacity, Comparator<T> comparator) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Capacity must be >= 1");
        }
        this.heap = new Object[initialCapacity];
        this.size = 0;
        this.comparator = comparator;
    }

    /**
     * Insertar un elemento en la cola
     * Complejidad: O(log n)
     */
    public void offer(T element) {
        if (element == null) {
            throw new NullPointerException("Cannot add null element");
        }

        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
    }

    /**
     * Alias para offer (mantiene compatibilidad)
     */
    public boolean add(T element) {
        offer(element);
        return true;
    }

    /**
     * Obtener y remover el elemento con mayor prioridad
     * Complejidad: O(log n)
     */
    @SuppressWarnings("unchecked")
    public T poll() {
        if (isEmpty()) {
            return null;
        }

        T result = (T) heap[0];
        size--;

        if (size > 0) {
            heap[0] = heap[size];
            heap[size] = null;
            siftDown(0);
        } else {
            heap[0] = null;
        }

        return result;
    }

    /**
     * Obtener el elemento con mayor prioridad sin removerlo
     * Complejidad: O(1)
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return (T) heap[0];
    }

    /**
     * Remover un elemento específico
     * Complejidad: O(n)
     */
    @SuppressWarnings("unchecked")
    public boolean remove(T element) {
        if (element == null) {
            return false;
        }

        // Buscar el elemento
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (element.equals(heap[i])) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false;
        }

        // Remover el elemento
        size--;
        if (index == size) {
            heap[index] = null;
        } else {
            heap[index] = heap[size];
            heap[size] = null;

            // Restaurar la propiedad del heap
            siftDown(index);
            if (heap[index] != null && heap[index].equals(heap[index])) {
                siftUp(index);
            }
        }

        return true;
    }

    /**
     * Verificar si la cola contiene un elemento
     * Complejidad: O(n)
     */
    public boolean contains(T element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(heap[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verificar si la cola está vacía
     * Complejidad: O(1)
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Obtener el tamaño de la cola
     * Complejidad: O(1)
     */
    public int size() {
        return size;
    }

    /**
     * Limpiar la cola
     * Complejidad: O(n)
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    /**
     * Mover un elemento hacia arriba en el heap
     * Restaura la propiedad del min-heap hacia arriba
     * Complejidad: O(log n)
     */
    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        T element = (T) heap[index];

        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            T parent = (T) heap[parentIndex];

            // Si el elemento es mayor o igual que el padre, detener
            if (comparator.compare(element, parent) >= 0) {
                break;
            }

            // Intercambiar con el padre
            heap[index] = parent;
            index = parentIndex;
        }

        heap[index] = element;
    }

    /**
     * Mover un elemento hacia abajo en el heap
     * Restaura la propiedad del min-heap hacia abajo
     * Complejidad: O(log n)
     */
    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        T element = (T) heap[index];
        int half = size / 2;

        while (index < half) {
            int childIndex = 2 * index + 1; // Hijo izquierdo
            T child = (T) heap[childIndex];
            int rightIndex = childIndex + 1;

            // Encontrar el hijo más pequeño
            if (rightIndex < size &&
                    comparator.compare(child, (T) heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = (T) heap[rightIndex];
            }

            // Si el elemento es menor o igual que el hijo más pequeño, detener
            if (comparator.compare(element, child) <= 0) {
                break;
            }

            // Intercambiar con el hijo más pequeño
            heap[index] = child;
            index = childIndex;
        }

        heap[index] = element;
    }

    /**
     * Asegurar capacidad suficiente
     * Complejidad: O(n) cuando se redimensiona
     */
    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            Object[] newHeap = new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    /**
     * Convertir a arreglo
     * Complejidad: O(n)
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return (T[]) result;
    }

    /**
     * Obtener representación en String
     */
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Verificar si el heap mantiene la propiedad de orden
     * Útil para debugging y testing
     */
    @SuppressWarnings("unchecked")
    public boolean isValid() {
        for (int i = 0; i < size; i++) {
            int leftChild = 2 * i + 1;
            int rightChild = 2 * i + 2;

            if (leftChild < size &&
                    comparator.compare((T) heap[i], (T) heap[leftChild]) > 0) {
                return false;
            }

            if (rightChild < size &&
                    comparator.compare((T) heap[i], (T) heap[rightChild]) > 0) {
                return false;
            }
        }
        return true;
    }
}
