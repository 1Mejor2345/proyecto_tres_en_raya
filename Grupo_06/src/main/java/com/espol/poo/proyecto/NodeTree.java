package com.espol.poo.proyecto;

import java.util.LinkedList;

/**
 * TDA Nodo de Árbol N-ario.
 * Profesora: Adriana Collaguazo Jaramillo
 */
public class NodeTree<E> {

    private E content;
    // Esa lista de hijos es una colección de otros árboles
    private LinkedList<Tree<E>> children;

    public NodeTree() {
        this.content = null;
        this.children = null;
    }

    public NodeTree(E content) {
        this.content = content;
        this.children = new LinkedList<>();
    }

    public E getContent() {
        return content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public LinkedList<Tree<E>> getChildren() {
        return children;
    }

    public void setChildren(LinkedList<Tree<E>> children) {
        this.children = children;
    }
}
