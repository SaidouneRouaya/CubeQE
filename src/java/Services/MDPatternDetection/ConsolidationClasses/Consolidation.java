package Services.MDPatternDetection.ConsolidationClasses;


import Services.MDfromLogQueries.Util.TdbOperation;
import org.apache.jena.rdf.model.*;

import java.util.*;


public class Consolidation {




    public static void main(String[] args) {
        HashMap<String, Model> modelHashMap = TdbOperation.unpersistModelsMap(TdbOperation.originalDataSet);

        System.out.println("******************* taille de Hashmap : " + modelHashMap.size());

        HashMap<String, Model> modelHashMapResult = consolidate(modelHashMap.values());

        System.out.println("******************* taille de Hashmap result : " + modelHashMapResult.size());
        System.out.println(" ----------------------------- finish --------------------------------------");

    }


    public static HashMap<String, Model> consolidate(Collection<Model> modelArrayList) {


        if (modelArrayList.size() == 0) {
            System.out.println("\nresults vide\n");
            return null;
        }

        toStringModelHashMap(modelArrayList);

        HashMap<String, Model> resultsHashMap = TdbOperation.unpersistModelsMap(TdbOperation.originalDataSet);

        return consolidate(resultsHashMap);
    }


    /** Transforms an Array list of models to a hashmap
     * where the key is a subject and the value is the corresponding model
     */

    public static void toStringModelHashMap(Collection<Model> modelArrayList) {
        Statement statement;
        String subject;
        int num = 0;
        HashMap<String, Model> modelHashMap = new HashMap<>();
        // create a pair <String, Model> where the key (String à is the subject of the statements that compose the model (value)

        // For every model in modelArrayList

        Iterator<Model> it = modelArrayList.iterator();

        while (it.hasNext()) {
            Model m = it.next();
            num++;
            System.out.println(" model num : " + num);
            Iterator<Statement> list = m.listStatements();
            // For every Statement in the model
            while (list.hasNext()) {
                statement = list.next();
                subject = statement.getSubject().toString();

                // if the pair doesn't exist in the map create a new instance
                if (!modelHashMap.containsKey(subject)) {
                    modelHashMap.put(subject, ModelFactory.createDefaultModel());
                    modelHashMap.get(subject).add(statement);
                } else {
                    // add the statement to the corresponding model
                    modelHashMap.get(subject).add(statement);
                }
            }
            if (num == 10000) {
                System.out.println("taille avant persist  " + modelHashMap.size());
                TdbOperation.persistNonAnnotated(modelHashMap, TdbOperation._toString);
                modelHashMap.clear();
                num = 0;
            }

            modelArrayList.remove(m);
            it = modelArrayList.iterator();
        }

        // return modelHashMap;
    }

    public static HashMap<String,Model> toStringModelsHashmap2(ArrayList<Model> modelArrayList)
    {
        HashMap<String,Model> modelHashMap = new HashMap<>();
        HashMap<String,Model> modelsFromOneModel;
        int nb = 0;
        for (Model m : modelArrayList)
        {
            modelsFromOneModel =  getModelsofModel(m);
            for (String key : modelsFromOneModel.keySet())
            {
                nb++;
                System.out.println("model num "+nb);
                if(modelHashMap.containsKey(key))
                {
                    modelHashMap.replace(key,modelHashMap.get(key).union(modelsFromOneModel.get(key)));
                }
                else
                {
                    modelHashMap.put(key, modelsFromOneModel.get(key));
                }
            }
        }
        return modelHashMap;
    }

    public static HashMap<String,Model> getModelsofModel(Model model)
    {
        List<Resource> resourceList = model.listSubjects().toList();
        List<RDFNode> rdfNodeList = model.listObjects().toList();
        HashMap<String,Model> modelHashMap = new HashMap<>();
        for (Resource resource : resourceList )
        {
            Model resourceModel = ModelFactory.createDefaultModel();
            if (!rdfNodeList.contains(resource))
            {
                ArrayList<RDFNode> visitedNodes = new ArrayList<>();
                visitedNodes.add(resource);
                modelHashMap.put(resource.getURI(),getModelOfResource(resource,resourceModel,visitedNodes));
            }
        }
        return modelHashMap;
    }

    public static Model getModelOfResource(Resource resource, Model model, ArrayList<RDFNode> visitedNodes )
    {
        StmtIterator stmtIterator = resource.listProperties();
        Model internModel = stmtIterator.toModel();
        //System.out.println(" le modele louwel "+resource+" "+internModel);
        //System.out.println(" modeeelddd "+model);
        List<Statement> list =resource.listProperties().toList();
        for(Statement statement : list)
        {
            //System.out.println("je rentre ici");
            //System.out.println(" modeeel "+model);
            boolean contains  = visitedNodes.contains(statement.getObject().asResource());
            //System.out.println("le contains "+contains);
            if (!contains)
            //if (model.getResource(rdfNode.toString()))
            {
                visitedNodes.add(statement.getObject());
                internModel.add(getModelOfResource(statement.getObject().asResource(), internModel, visitedNodes));
            }
            else{
                internModel = internModel.remove(statement);
            }
            //internModel = internModel.union(getModelOfResource(statement.getObject().asResource(),internModel));
            //model.add();
        }
        return internModel;
    }

    /**
     * Consolidates the given model map so that all models (values) are mutually independent
     * (two model are independent if there is no node shared between them)
     */

    public static HashMap<String, Model> consolidate(HashMap<String, Model> modelsHashMap) {

        int sizeOfResults = modelsHashMap.size();
        int newSizeOfResults = 0; // to compare it with the old one and exit the loop
        NodeIterator nodeIterator;

        // loop until there is no connsolidation possible i.e. the size of the map doesn't change
        while (sizeOfResults != newSizeOfResults) {
            Set<String> kies = modelsHashMap.keySet();
            sizeOfResults = newSizeOfResults;

            for (String key : kies) {

                nodeIterator = modelsHashMap.get(key).listObjects();
                // for all nodes in modelsHashMap
                while (nodeIterator.hasNext()) {
                    RDFNode node = nodeIterator.next();

                    // if node already exists as key (subject) in the map, and its model is not empty
                    if (modelsHashMap.containsKey(node.toString()) && !modelsHashMap.get(node.toString()).isEmpty()) {

                        // then consolidate it with the model in question
                        modelsHashMap.get(key).add(modelsHashMap.get(node.toString()));
                        modelsHashMap.put(node.toString(), ModelFactory.createDefaultModel());
                    }
                }
            }

            // clean the map from the empty models
            modelsHashMap = cleanMap(modelsHashMap);
            newSizeOfResults = modelsHashMap.size();
        }
        return modelsHashMap;
    }

    /**
     * Cleans the given map from paris with empty values
     */
    public static HashMap<String, Model> cleanMap(HashMap<String, Model> map) {

        HashMap<String, Model> newResults = new HashMap<>();

        Iterator it = map.entrySet().iterator();
        Map.Entry<String, Model> pair;

        while (it.hasNext()) {

            pair = (Map.Entry) it.next();
            if (!pair.getValue().isEmpty()) {
                newResults.put(pair.getKey(),pair.getValue());
            }
        }
        return newResults;
    }

    //TODO enlever cella
    public static void afficherListInformations(HashMap<String, Model> listInfoNodes) {

        Iterator it = listInfoNodes.entrySet().iterator();

        System.out.println(" Affichage des résultats \n");
        int i =0;

        while (it.hasNext()) {
            Map.Entry<String, Model> pair = (Map.Entry) it.next();
            i++;
            System.out.println(" Subject "+i+" : \t\t " + pair.getKey() + "\n");
            Iterator<Statement> listStatements = pair.getValue().listStatements();
            while (listStatements.hasNext()) {
                System.out.println(listStatements.next().toString());
            }

            System.out.println("\n______________________________________________________________________\n");

        }
    }
    public static void afficherListInformationsSet(HashSet< Model> listInfoNodes) {

        System.out.println(" Afichage des résultats \n");
        int i =0;

        if (listInfoNodes.isEmpty()) System.out.println("liste vide");
        for (Model m: listInfoNodes)
        {

            System.out.println(" Subject "+i+" : \t\t " + m.listSubjects().nextResource().toString() + "\n");

            Iterator<Statement> listStatements = m.listStatements();
            while (listStatements.hasNext()) {
                System.out.println(listStatements.next().toString());

            }

            System.out.println("\n______________________________________________________________________\n");

        }
    }



}

