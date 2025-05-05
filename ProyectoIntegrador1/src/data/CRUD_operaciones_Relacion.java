package data;

import java.util.ArrayList;

public interface CRUD_operaciones_Relacion <S,T,U>{
	
	void save(S entity);
    ArrayList<S> fetch();
    void update(S entity);
    void delete(T id1, U id2);
    boolean authenticate(T id1, U id2);
}
