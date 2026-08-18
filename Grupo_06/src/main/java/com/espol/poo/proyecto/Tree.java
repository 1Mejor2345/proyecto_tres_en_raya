package com.espol.poo.proyecto;

/**
 * TDA Árbol N-ario genérico.
 * Profesora: Adriana Collaguazo Jaramillo
 */
public class Tree<E> {

    // Un árbol solo conoce a su nodo raíz, que es de tipo NodeTree
    private NodeTree<E> root;

    public Tree() {
        this.root = null;
    }

    public NodeTree<E> getRoot() {
        return root;
    }

    public void setRoot(NodeTree<E> root) {
        this.root = root;
    }

    public boolean isEmpty() {
        return root == null;
    }
}
