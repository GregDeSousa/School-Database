public class Treap<T extends Comparable<T>> {
    public Node<T> root = null;

    @Override
    public String toString() {
        if (root == null) {
            return "";
        }

        return root.toString() + "\n" + toString(root, "");
    }

    private String toString(Node<T> curr, String pre) {
        if (curr == null)
            return "";
        String res = "";

        if (curr.left != null) {
            if (curr.right != null) {
                res += pre + "├(L)─ " + curr.left.toString() + "\n" + toString(curr.left, pre + "|    ");
            } else {
                res += pre + "└(L)─ " + curr.left.toString() + "\n" + toString(curr.left, pre + "     ");
            }
        }

        if (curr.right != null) {
            res += pre + "└(R)─ " + curr.right.toString() + "\n" + toString(curr.right, pre + "   ");
        }
        return res;
    }

    /*
     * Don't change anything above this line
     */

    public void insert(T data) throws DatabaseException {
        if(this.root==null){
            root=new Node<>(data);
        }else{
            Node<T> node=root;
            Node<T> prev=null;
            while(node!=null && !node.getData().equals(data)){
                prev=node;
                if(node.getData().compareTo(data)<0){
                    node=node.right;
                }else{
                    node=node.left;
                }
            }

            if(node==null){
                if (prev.getData().compareTo(data) > 0) {
                    prev.left = new Node<>(data);
                } else {
                    prev.right = new Node<>(data);
                }
                Balance(root,data,null);
            }else {
                throw DatabaseException.duplicateInsert( data);
            }

        }
    }

    public Node<T> remove(T data) {
        if(root==null){
            return null;
        }else{
            Node<T> prev=null;
            Node<T> node=root;
            while(node!=null && !node.getData().equals(data)){
                if(node.getData().compareTo(data)>0){
                    prev=node;
                    node=node.left;
                }else{
                    prev=node;
                    node=node.right;
                }
            }
            if(node==null){
                return null;
            }
            if(root.left==null&&root.right==null){
                Node<T> temp=root;
                root=null;
                return temp;
            }
            while(!Onechild(node)){
                if(node.right!=null && node.left!=null) {
                    Node<T> temp;
                    if (node.left.priority > node.right.priority) {
                        temp = node.left;
                        RotateRight(prev, node);
                    } else {
                        temp = node.right;
                        RotateLeft(prev, node);
                    }
                    prev = temp;
                }else{
                    Node<T> temp;
                    if(node.left==null){
                        temp = node.right;
                        RotateLeft(prev, node);
                    }else{
                        temp = node.left;
                        RotateRight(prev, node);
                    }
                    prev=temp;
                }
            }
            //System.out.println(this.toString());
            if(prev==null){
                if(node.right==null){
                    root=node.left;
                    node.left=null;
                }else{
                    root=node.right;
                    node.right=null;
                }
                return node;
            }
            if(data.compareTo(prev.getData())<0){
                if(node.right==null && node.left==null){
                    prev.left=null;
                }else{
                    if(node.right==null){
                        prev.left=node.left;
                    }else{
                        prev.left=node.right;
                    }
                }
            }else{
                if(node.right==null && node.left==null){
                    prev.right=null;
                }else{
                    if(node.right==null){
                        prev.right=node.left;
                    }else{
                        prev.right=node.right;
                    }
                }
            }
            return node;
        }
    }

    public Node<T> access(T data) {
        if(this.root==null){
            return null;
        }else{
            Node<T> node=root;
            Node<T> prev=null;
            if(data.equals(root.getData())){
                node.priority++;
                return node;
            }
            while(node!=null){
                if(node.right!=null){
                    if(node.right.getData().equals(data)){
                        break;
                    }
                }
                if(node.left!=null){
                    if(node.left.getData().equals(data)){
                        break;
                    }
                }
                if(node.getData().compareTo(data)>0){
                    prev=node;
                    node=node.left;
                }else{
                    prev=node;
                    node=node.right;
                }
            }
            if(node!=null){
                if (node.right!=null && node.right.getData().equals(data)) {
                    node.right.priority++;
                    Node<T> temp=node.right;
                    Balance(root,data,null);
                    return temp;
                }else{
                    if(node.left!=null) {
                        node.left.priority++;
                        Node<T> temp=node.left;
                        Balance(root,data,null);
                        return temp;
                    }
                }
                //System.out.println(node.getData() +":" + data + ":" + prev.getData());
                //Balance(root,data,null);
                return node;
            }else{
                return null;
            }
        }
    }

    private void Balance(Node<T> Parent,T data,Node<T> prev){
        if(Parent!=null){
            if(data.compareTo(Parent.getData())>0){
                Balance(Parent.right,data,Parent);
                if(Parent.right!=null && Parent.right.priority>=Parent.priority){
                    if(Parent.priority==Parent.right.priority && Parent.isRot && Parent.right.isRot && !Parent.getData().equals(data) && !Parent.right.getData().equals(data)){
                        return;
                    }
                    Parent.right.isRot=true;
                    RotateLeft(prev, Parent);
                }
            }else{
                Balance(Parent.left,data,Parent);
                if(Parent.left!=null && Parent.left.priority>=Parent.priority){
                    if(Parent.priority==Parent.left.priority && Parent.isRot && Parent.left.isRot && !Parent.getData().equals(data) && !Parent.left.getData().equals(data)){
                        return;
                    }
                    Parent.left.isRot=true;
                    RotateRight(prev, Parent);
                }
            }
        }
    }

    private void RotateLeft(Node<T> subroot,Node<T> parent){
        if (parent!=null){
            if(subroot!=null) {
                if(subroot.getData().compareTo(parent.getData())>0) {
                    subroot.left = parent.right;
                }else{
                    subroot.right=parent.right;
                }
            }
            if(parent.right!=null) {
                Node<T> temp = parent.right.left;
                parent.right.left = parent;
                if(parent==root){
                    root=parent.right;
                }
                parent.right = temp;
            }
        }
    }

    private void RotateRight(Node<T> subroot,Node<T> parent){
        if (parent!=null){
            if(subroot!=null) {
                if(subroot.getData().compareTo(parent.getData())>0) {
                    subroot.left = parent.left;
                }else{
                    subroot.right=parent.left;
                }
            }
            if(parent.left!=null) {
                Node<T> temp = parent.left.right;
                parent.left.right = parent;
                if(parent==root){
                    root=parent.left;
                }
                parent.left = temp;
            }
        }
    }

    private boolean Onechild(Node<T> n){
        if(n==null){
            return false;
        }else{
            if((n.right == null && n.left != null)||(n.right != null && n.left == null)){
                if(n.right == null){
                    return n.left.left == null && n.left.right == null;
                }else{
                    return n.right.left == null && n.right.right == null;
                }
            }else{
                return n.right == null;
            }
        }
    }

    public Node<T> accessN(T data) {
        if(this.root==null){
            return null;
        }else{
            Node<T> node=root;
            Node<T> prev=null;
            if(data.equals(root.getData())){
                return node;
            }
            while(node!=null){
                if(node.right!=null){
                    if(node.right.getData().equals(data)){
                        break;
                    }
                }
                if(node.left!=null){
                    if(node.left.getData().equals(data)){
                        break;
                    }
                }
                if(node.getData().compareTo(data)>0){
                    prev=node;
                    node=node.left;
                }else{
                    prev=node;
                    node=node.right;
                }
            }
            if(node!=null){
                if (node.right!=null && node.right.getData().equals(data)) {
                    return node.right;
                }else{
                    if(node.left!=null) {
                        return node.left;
                    }
                }
                return node;
            }else{
                return null;
            }
        }
    }
}




/*incase you need it back
if(prev!=null) {
        if (node.right == null && node.left == null) {
        if (prev.getData().compareTo(node.getData()) > 0) {
        prev.right = null;
        } else {
        prev.left = null;
        }
        } else {
        if (node.right == null) {
        if (prev.getData().compareTo(node.getData()) > 0) {
        prev.left = node.left;
        } else {
        prev.right = node.left;
        }
        } else {
        if (prev.getData().compareTo(node.getData()) > 0) {
        prev.left = node.right;
        } else {
        prev.right = node.right;
        }
        }
        }
        }else{
        if(node.right == null && node.left == null){
        root=null;
        return node;
        }else{
        if(node.right!=null){
        root=node.right;
        node.right=null;
        }else{
        root=node.left;
        node.left=null;
        }
        }
        return node;

        }
        return node;*/

/* private void Balance(Node<T> Parent,T data,Node<T> prev){//TO
     Node<T> node=root;
            Node<T> prev=null;
            if(data.equals(root.getData())){
                node.priority++;
                return node;
            }
            while(node!=null){
                if(node.right!=null){
                    if(node.right.getData().equals(data)){
                        break;
                    }
                }
                if(node.left!=null){
                    if(node.left.getData().equals(data)){
                        break;
                    }
                }
                if(node.getData().compareTo(data)>0){
                    prev=node;
                    node=node.left;
                }else{
                    prev=node;
                    node=node.right;
                }
            }
            if(node!=null){
                if (node.right!=null && node.right.getData().equals(data)) {

                    return node.right;
                }else{
                    if(node.left!=null) {

                        return node.left;
                    }
                }


    }
*/