package com.lm.hiketracker.hiketracker.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.Gson;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FirestoreService {

  public String saveDocument(Object object, String collectionPath)
      throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference docRef = db.collection(collectionPath).document();
    Map<String, Object> objectWithoutNulls = removeNullFieldsFromObject(object);
    docRef.set(objectWithoutNulls).get();
    return docRef.getId();
  }

  /**
   * Finds a document in Firestore DB by ID. If found, it parses the Document Reference to an object
   * of the class passed as parameter and returns it.
   *
   * @param collectionPath Collection where the document searched is stored.
   * @param documentId ID of the document beign searched.
   * @param responseClass Class to which the document should be parsed.
   * @return Returns the document parsed to the type passed as parameter.
   * @param <T> Type to which the document should be parsed to.
   * @throws ExecutionException Exception thrown by Firestore SDK.
   * @throws InterruptedException Exception thrown by Firestore SDK.
   */
  public <T> T findByDocumentId(String collectionPath, String documentId, Class<T> responseClass)
      throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference docRef = db.collection(collectionPath).document(documentId);
    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document = future.get();
    if (document.exists()) {
      return document.toObject(responseClass);
    } else {
      return null;
    }
  }

  public void updateDocument(String collectionPath, String documentId, Object newObject)
      throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference docRef = db.collection(collectionPath).document(documentId);
    Map<String, Object> objectWithoutNulls = removeNullFieldsFromObject(newObject);
    docRef.set(objectWithoutNulls).get();
  }

  private Map<String, Object> removeNullFieldsFromObject(Object object) {
    Gson gson = new Gson();
    // Convert object to a map
    Map<String, Object> map = gson.fromJson(gson.toJson(object), Map.class);
    // Remove null values
    Iterator<Entry<String, Object>> it = map.entrySet().iterator();
    while (it.hasNext()) {
      Map.Entry<String, Object> entry = it.next();
      if (entry.getValue() == null) {
        it.remove();
      }
    }
    return map;
  }

  public <T> List<T> findAllByCollection(String collectionPath, Class<T> responseClass)
      throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    ApiFuture<QuerySnapshot> future = db.collection(collectionPath).get();
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    List<T> results = new ArrayList<>();

    for (DocumentSnapshot document : documents) {
      T object = document.toObject(responseClass);
      if (object != null) {
        try {
          Method setDocumentIdMethod = responseClass.getMethod("setDocumentId", String.class);
          setDocumentIdMethod.invoke(object, document.getId());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
          log.error(e.getMessage());
        }
        results.add(object);
      }
    }
    return results;
  }
}
