package com.yesmywine.security.redis;

import org.apache.shiro.codec.Base64;
import org.apache.shiro.io.SerializationException;
import org.apache.shiro.session.Session;

import java.io.*;

/**
 * Created by SJQ on 2017/5/27.
 */
public class ShiroSerializeUtils {
    /**
     * <p>SerializationUtils instances should NOT be constructed in standard programming.
     * Instead, the class should be used as {@code SerializationUtils.clone(object)}.</p>
     *
     * <p>This constructor is public to permit tools that require a JavaBean instance
     * to operate.</p>
     * @since 2.0
     */
    public ShiroSerializeUtils() {
        super();
    }


    // Serialize
    //-----------------------------------------------------------------------
    public static void serialize(Serializable obj, OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("The OutputStream must not be null");
        }
        ObjectOutputStream out = null;
        try {
            // stream closed in the finally
            out = new ObjectOutputStream(outputStream);
            out.writeObject(obj);

        } catch (IOException ex) {
            throw new SerializationException(ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) { // NOPMD
                // ignore close exception
            }
        }
    }

    public static byte[] serialize(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("serialize session error", e);
        }
    }

    // Deserialize
    public static Object deserialize(InputStream inputStream) {
        if (inputStream == null) {
            throw new IllegalArgumentException("The InputStream must not be null");
        }
        ObjectInputStream in = null;
        try {
            // stream closed in the finally
            in = new ObjectInputStream(inputStream);
            return in.readObject();

        } catch (ClassNotFoundException ex) {
            throw new SerializationException(ex);
        } catch (IOException ex) {
            throw new SerializationException(ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) { // NOPMD
                // ignore close exception
            }
        }
    }

    public static Object deserialize(byte[] bytes) {

        		Object result = null;

        		if (bytes==null) {
        			return null;
        		}

        		try {
        			ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
            			try {
                				ObjectInputStream objectInputStream = new ObjectInputStream(byteStream);
                				try {
                					result = objectInputStream.readObject();
                				}
                			catch (ClassNotFoundException ex) {
                					throw new Exception("Failed to deserialize object type", ex);
                				}
                			}
            			catch (Throwable ex) {
                				throw new Exception("Failed to deserialize", ex);
                			}
            		} catch (Exception e) {
//            			logger.error("Failed to deserialize",e);
            		}
        		return result;
        	}

    static class ClassLoaderAwareObjectInputStream extends ObjectInputStream {
        private ClassLoader classLoader;

        public ClassLoaderAwareObjectInputStream(InputStream in, ClassLoader classLoader) throws IOException {
            super(in);
            this.classLoader = classLoader;
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            String name = desc.getName();
            try {
                return Class.forName(name, false, classLoader);
            } catch (ClassNotFoundException ex) {
                return Class.forName(name, false, Thread.currentThread().getContextClassLoader());
            }
        }

    }
}
