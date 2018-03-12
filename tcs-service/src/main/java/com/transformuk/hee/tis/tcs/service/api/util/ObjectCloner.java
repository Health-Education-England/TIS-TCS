package com.transformuk.hee.tis.tcs.service.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectCloner {

  private static final Logger LOG = LoggerFactory.getLogger(ObjectCloner.class);

  // so that nobody can accidentally create an ObjectCloner object
  private ObjectCloner(){}

  // returns a deep copy of an object
  static public Object deepCopy(Object oldObj) throws Exception
  {
    ObjectOutputStream oos = null;
    ObjectInputStream ois = null;
    try
    {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(bos);
      // serialize and pass the object
      oos.writeObject(oldObj);
      oos.flush();
      ByteArrayInputStream bin = new ByteArrayInputStream(bos.toByteArray());
      ois = new ObjectInputStream(bin);
      // return the new object
      return ois.readObject();
    }
    catch(Exception e)
    {
      LOG.error("Exception in ObjectCloner = ",  e);
      throw(e);
    }
    finally
    {
      oos.close();
      ois.close();
    }
  }
}
