/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jsfproject.jsfmaven;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import model.Schueler;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author 20100435
 */
@ManagedBean(name = "schuelerCon")
@SessionScoped
public class SchuelerController implements Serializable {

    private List<Schueler> schuelerList;
    private List<Schueler> filteredSchuelerList;
    private List<Schueler> selectedSchueler;

    private TreeNode selectedPupil;
    private TreeNode rootNode;
    private Schueler schueler, schuelerDetails;

    @PostConstruct
    public void init() {
        schuelerList = Schueler.getKlasse();
        schueler = new Schueler();
        populateTree();
    }
// Test success
    private void populateTree() {
        rootNode = new DefaultTreeNode("Schüler");

        TreeNode AH = new DefaultTreeNode("bereich", "A-H", rootNode);
        TreeNode IZ = new DefaultTreeNode("bereich", "I-Z", rootNode);

        List<Schueler> schAH = new ArrayList<>();
        List<Schueler> schIZ = new ArrayList<>();

        for (Schueler s : schuelerList) {
            char c = s.getNachname().toUpperCase().charAt(0);
            if (c >= 65 && c <= 72) {
                schAH.add(s);
            } else {
                schIZ.add(s);
            }
        }

        Collections.sort(schAH);
        Collections.sort(schIZ);

        for (Schueler s : schAH) {
            TreeNode t1 = new DefaultTreeNode("schueler", s, AH);
        }
        for (Schueler s : schIZ) {
            TreeNode t2 = new DefaultTreeNode("schueler", s, IZ);
        }

//        schAH.forEach((Schueler s) -> {TreeNode t1 = new DefaultTreeNode("A-H",s, AH);});
//        schIZ.forEach((Schueler s) -> {TreeNode t2 = new DefaultTreeNode("I-Z",s, IZ);});
    }

    public boolean filterCaseInsensitive(Object value, Object filter, Locale locale) {
        String fText = (filter != null) ? filter.toString().toLowerCase().trim() : null;
        if (fText == null || "".equals(fText)) {
            return true;
        }
        if (value == null) {
            return false;
        }

        if (value.toString().toLowerCase().contains(fText)) {
            return true;
        }
        return false;
    }

    public String redirect() {
        if (selectedSchueler.size() > 0) {
            return "weiter";
        }
        return null;
    }

    public void showPupilDetails(NodeSelectEvent e) {
        if (e != null) {
            if ("bereich".equals(e.getTreeNode().getType())) {
                int c = e.getTreeNode().getChildCount();
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "Anzahl der Schüler: " + c, ""));
            } else if ("schueler".equals(e.getTreeNode().getType())) {
                schuelerDetails = findSchueler((Schueler) e.getTreeNode().getData());
                RequestContext.getCurrentInstance().execute("showPupil()");
            }
        }
    }

    public void schuelerSpeichern() {
        if (schueler != null && !schueler.getVorname().isEmpty()
                && !schueler.getNachname().isEmpty()) {
            TreeNode AH = rootNode.getChildren().get(0);
            TreeNode IZ = rootNode.getChildren().get(1);
            char c = schueler.getNachname().toUpperCase().charAt(0);

            schueler.setNr(schuelerList.size() + 1);
            schuelerList.add(schueler);

            if (c >= 65 && c <= 72) {
                AH.getChildren().add(new DefaultTreeNode("schueler",schueler, AH));
                AH.setExpanded(true);
            } else {
                IZ.getChildren().add(new DefaultTreeNode("schueler",schueler, IZ));
                IZ.setExpanded(true);
            }
            schueler = new Schueler();
        }
    }

    private Schueler findSchueler(Schueler schueler) {
        for (Schueler s : schuelerList) {
            if(schueler.getNr() == s.getNr())
                return s;
        }
        return null;
    }

    public Schueler getSchuelerDetails() {
        return schuelerDetails;
    }

    public void setSchuelerDetails(Schueler schuelerDetails) {
        this.schuelerDetails = schuelerDetails;
    }

    public Schueler getSchueler() {
        return schueler;
    }

    public void setSchueler(Schueler schueler) {
        this.schueler = schueler;
    }

    public TreeNode getSelectedPupil() {
        return selectedPupil;
    }

    public void setSelectedPupil(TreeNode selectedPupil) {
        this.selectedPupil = selectedPupil;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public List<Schueler> getSelectedSchueler() {
        return selectedSchueler;
    }

    public void setSelectedSchueler(List<Schueler> selectedSchueler) {
        this.selectedSchueler = selectedSchueler;
    }

    public List<Schueler> getFilteredSchuelerList() {
        return filteredSchuelerList;
    }

    public void setFilteredSchuelerList(List<Schueler> filteredSchuelerList) {
        this.filteredSchuelerList = filteredSchuelerList;
    }

    public List<Schueler> getSchuelerList() {
        return schuelerList;
    }

    public void setSchuelerList(List<Schueler> schuelerList) {
        this.schuelerList = schuelerList;
    }

}
