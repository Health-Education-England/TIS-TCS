package com.transformuk.hee.tis.tcs.service.api.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectCloner {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectCloner.class);

  // so that nobody can accidentally create an ObjectCloner object
  private ObjectCloner() {
  }

  // returns a deep copy of an object
  public static <K> K deepCopy(K oldObj) throws IOException, ClassNotFoundException {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      // serialize and pass the object
      oos.writeObject(oldObj);
      oos.flush();
      ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
      ois = new ObjectInputStream(bin);
      // return the new object
      return (K) ois.readObject();
    } catch (IOException e) {
      LOG.error("IO error while deep copying the object", e);
      throw e;
    } catch (ClassNotFoundException e) {
      LOG.error("Error while deep copying the object", e);
      throw e;
    } finally {
      if (oos != null) {
        oos.close();
      }
      if (ois != null) {
        ois.close();
      }
    }
  }
}
