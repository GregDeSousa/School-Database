@SuppressWarnings("unchecked")
public class Database {
    String[][] database;
    String[] columnNames;
    Treap<Cell>[] indexes;

    public Database(String[] cols, int maxSize) {
        database=new String[maxSize][cols.length];
        for(int i=0;i<maxSize;i++){
            for(int j=0;j<cols.length;j++) {
                database[i][j] = null;
            }
        }
        this.columnNames=cols;
        indexes=new Treap[cols.length];///////TODO:if some generic error happens on submission or further testing mabye change the initialization of this array
        for(int i=0;i<cols.length;i++){
            indexes[i]=null;
        }
    }

    public void insert(String[] newRowDetails) throws DatabaseException {
        if(newRowDetails.length==this.columnNames.length){
            for(int i=0;i<this.indexes.length;i++){
                Cell test=new Cell(-1,newRowDetails[i]);
                if(this.indexes[i]!=null) {
                    Node<Cell> c=this.indexes[i].access(test);
                    if (c != null) {
                        throw DatabaseException.duplicateInsert(c);
                    }
                }
            }

            int ind = 0;
            while (ind < this.database.length) {
                if(database[ind][0]==null){
                    break;
                }
                ind++;
            }
            if (ind == this.database.length) {
                throw DatabaseException.databaseFull();
            }

            for(int i=0;i<this.indexes.length;i++){
                Cell ins=new Cell(ind,newRowDetails[i]);
                if(this.indexes[i]!=null) {
                    try {
                        this.indexes[i].insert(ins);
                    } catch (DatabaseException d) {
                        throw d;
                    }
                }
            }

            for(int i=0;i<newRowDetails.length;i++){
                database[ind][i]=newRowDetails[i];
            }

        }else{
            throw DatabaseException.invalidNumberOfColums();
        }

    }

    public String[] removeFirstWhere(String col, String data) throws DatabaseException {
        int colind;
        if(indexOf(this.columnNames,col)==-1){
            throw DatabaseException.invalidColumnName(col);
        }else{
            colind=indexOf(this.columnNames,col);
        }
        String[] ret=null;
        if(this.indexes[colind]!=null){
            Cell temp=new Cell(-1,data);
            Node<Cell> n =indexes[colind].remove(temp);
            Cell found=null;
            if(n!=null){
                found=n.getData();
            }
            if(found!=null){
                ret=new String[this.columnNames.length];
                for(int z=0;z<columnNames.length;z++){
                    ret[z]=database[found.databaseRow][z];
                    database[found.databaseRow][z]=null;
                }
                for(int i=0;i<this.indexes.length;i++){
                    if(i!=colind && this.indexes[i]!=null){
                        this.indexes[i].remove(new Cell(-1,ret[i]));
                    }
                }
                return ret;
            }else{
                return new String[0];
            }
        }else{
            int i;
            for(i=0;i<this.database.length;i++){
                if(database[i][colind]==null){
                    continue;
                }
                if(database[i][colind].equals(data)){
                    ret=new String[this.columnNames.length];
                    for(int z=0;z<columnNames.length;z++){
                        ret[z]=database[i][z];
                        database[i][z]=null;
                    }
                    for(int z=0;z<this.indexes.length;z++){
                        if(z!=colind && this.indexes[z]!=null){
                            this.indexes[z].remove(new Cell(-1,ret[z]));
                        }
                    }
                    break;
                }
            }
            if(i==this.database.length){
                return new String[0];
            }
            return ret;
        }
    }

    public String[][] removeAllWhere(String col, String data) throws DatabaseException {
        String[][] removed=new String[0][0];
        int i=0;
        String[] del;
        while(true){
            try {
                del = removeFirstWhere(col, data);
            }catch(DatabaseException d){
                throw d;
            }
            if(del.length==0){
                break;
            }
            if(removed.length==0){
                removed=new String[countOccurences(col,data)+1][this.indexes.length];
            }
            removed[i]=del;
            i++;
        }

        return removed;
    }

    public String[] findFirstWhere(String col, String data) throws DatabaseException {
        if(this.database!=null && this.database.length!=0) {
            int colind;
            if (indexOf(this.columnNames, col) == -1) {
                throw DatabaseException.invalidColumnName(col);
            } else {
                colind = indexOf(this.columnNames, col);
            }
            String[] ret = null;
            if (this.indexes[colind] != null) {
                Cell temp = new Cell(-1, data);
                Cell found = null;
                Node<Cell> f = indexes[colind].access(temp);
                if (f != null) {
                    found =  f.getData();
                }
                if (found != null) {
                    ret = database[found.databaseRow];
                    return ret;
                } else {
                    return new String[0];
                }
            } else {
                int i;
                for (i = 0; i < this.database.length; i++) {
                    if(database[i][colind]==null){
                        continue;
                    }
                    if (database[i][colind].equals(data)) {
                        ret = database[i];
                        break;
                    }
                }
                if (i == this.database.length) {
                    return new String[0];
                }
                return ret;
            }
        }else{
            if (indexOf(this.columnNames, col) == -1) {
                throw DatabaseException.invalidColumnName(col);
            }
            return new String[0];
        }
    }

    public String[][] findAllWhere(String col, String data) throws DatabaseException {
        if(countOccurences(col,data)==0){
            return new String[0][0];
        }else{
            int colind;
            if(indexOf(this.columnNames,col)==-1){
                throw DatabaseException.invalidColumnName(col);
            }else{
                colind=indexOf(this.columnNames,col);
            }
            String[][] found=new String[countOccurences(col,data)][this.columnNames.length];
            if(indexes[colind]!=null) {
                Cell temp = new Cell(-1, data);
                Node<Cell>n=indexes[colind].access(temp);
                if(n==null){
                    return found;
                }else {
                    found[0] = database[n.getData().databaseRow];
                }
            }else{
                int j=0;
                for (String[] strings : this.database) {
                    if(strings[colind]==null){
                        continue;
                    }
                    if (strings[colind].equals(data)) {
                        found[j] = strings;
                        j++;
                    }
                }
            }
            return found;
        }
    }

    public String[] updateFirstWhere(String col, String updateCondition, String data) throws DatabaseException {
        if(this.database!=null && this.database.length!=0) {
            int colind;
            if (indexOf(this.columnNames, col) == -1) {
                throw DatabaseException.invalidColumnName(col);
            } else {
                colind = indexOf(this.columnNames, col);
            }

            String[] ret = null;
            if (this.indexes[colind] != null) {
                Cell temp = new Cell(-1, updateCondition);
                Cell update=null;
                Node<Cell> u=indexes[colind].remove(temp);
                if(u!=null) {
                    update = u.getData();
                    update.value = data;
                }
                if (update != null) {
                    try {
                        indexes[colind].insert(update);
                    } catch (DatabaseException d) {
                        throw d;
                    }
                    database[update.databaseRow][colind] = data;
                    ret = database[update.databaseRow];
                    return ret;
                } else {
                    return new String[0];
                }
            } else {
                int i;
                for (i = 0; i < this.database.length; i++) {
                    if(database[i][colind]==null){
                        continue;
                    }
                    if (database[i][colind].equals(updateCondition)) {
                        database[i][colind] = data;
                        ret = database[i];
                        break;
                    }
                }
                if (i == this.database.length) {
                    return new String[0];
                }
                return ret;
            }
        }else{
            if (indexOf(this.columnNames, col) == -1) {
                throw DatabaseException.invalidColumnName(col);
            }
            return new String[0];
        }
    }

    public String[][] updateAllWhere(String col, String updateCondition, String data) throws DatabaseException {
        String[][] found=new String[0][0];
        int i=0;
        String[] f;
        while(true){
            try {
                f = updateFirstWhere(col, updateCondition,data);
            }catch(DatabaseException d){
                throw d;
            }
            if(f.length==0){
                break;
            }
            if(found.length==0){
                found=new String[countOccurences(col,updateCondition)+1][this.database.length];
            }
            found[i]=f;
            i++;
        }

        return found;
    }

    public Treap<Cell> generateIndexOn(String col) throws DatabaseException {
        if(this.isntEmpty()) {
            int colind;
            if (indexOf(this.columnNames, col) == -1) {
                throw DatabaseException.invalidColumnName(col);
            } else {
                colind = indexOf(this.columnNames, col);
            }

            if (this.indexes[colind] == null) {
                this.indexes[colind]=new Treap<>();
                for (int i = 0; i < this.database.length; i++) {
                    if (database[i][0] != null) {
                        Cell ins = new Cell(i, database[i][colind]);
                        try {
                            this.indexes[colind].insert(ins);
                        } catch (DatabaseException d) {
                            this.indexes[colind] = null;
                            throw d;
                        }
                    }
                }
            }
            return this.indexes[colind];
        }else{
            if (indexOf(this.columnNames, col) == -1) {
                throw DatabaseException.invalidColumnName(col);
            }
            return new Treap<>();
        }
    }

    public Treap<Cell>[] generateIndexAll() throws DatabaseException {
        if(this.isntEmpty()) {
            for (int i = 0; i < this.indexes.length; i++) {
                try {
                    generateIndexOn(this.columnNames[i]);
                } catch (DatabaseException d) {
                    //Swallow ;)
                }
            }
        }else{
            for(int i=0;i<this.indexes.length;i++){
                this.indexes[i]=new Treap<>();
            }
        }
        return this.indexes;
    }

    public int countOccurences(String col, String data) throws DatabaseException {
        int colind;
        if (indexOf(this.columnNames, col) == -1) {
            throw DatabaseException.invalidColumnName(col);
        } else {
            colind = indexOf(this.columnNames, col);
        }
        int count=0;
        for(int i=0;i<this.database.length;i++){
            if(database[i][colind]==null){
                continue;
            }
            if(database[i][colind].equals(data)){
                count++;
            }
        }
        return count;
    }

    private int indexOf(String[] arr,String data){
        for(int i=0;i<arr.length;i++){
            if(arr[i].equals(data)){
                return i;
            }
        }
        return -1;
    }
    private boolean isntEmpty(){
        if(this.database==null || this.database.length==0){
            return true;
        }
        for(int i=0;i<this.database.length;i++){
            if(database[i][0]!=null){
                return true;
            }
        }
        return false;
    }
}

/*if(removed.length!=0) {
               String[][] temp = new String[removed.length][removed[0].length];
               for(int z=0;z<removed.length;z++){
                   for(int x=0;x<removed[0].length;x++){
                       temp[z][x]=removed[z][x];
                   }
               }
               removed = new String[i][this.columnNames.length];
               for(int z=0;z<temp.length;z++){
                   for(int x=0;x<temp[0].length;x++){
                       removed[z][x]=temp[z][x];
                   }
               }
               removed[removed.length-1]=del;
           }else{
               removed=new String[1][this.columnNames.length];
           }*/