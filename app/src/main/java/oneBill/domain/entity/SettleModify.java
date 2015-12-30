package oneBill.domain.entity;

import java.util.Iterator;
import java.util.ArrayList;

import oneBill.domain.entity.error.UnableToClearException;

public class SettleModify {
    public SettleModify() {
    }

    public class Point {
        public String name;
        public ArrayList<String> neighbor;
        public Point(String name) {
            this.name = name;
            this.neighbor = new ArrayList<String>();
        }
    }

    public static void settlePerson(Point point, ArrayList<Point> pointVec,ArrayList<String> pointVecName) {
        pointVec.remove(point);
        pointVecName.remove(point.name);
        Iterator<Point> poIterator = pointVec.iterator();
        while (poIterator.hasNext())
            poIterator.next().neighbor.remove(point.name);
    }

    public static String findMostLimit(ArrayList<Point> pointVec) {
        int degree = pointVec.size();
        String mostLimit = null;
        Iterator<Point> poIterator = pointVec.iterator();
        while (poIterator.hasNext()) {
            Point thePoint = poIterator.next();
            if (degree > thePoint.neighbor.size()) {
                degree = thePoint.neighbor.size();
                mostLimit = thePoint.name;
            }
        }
        return mostLimit;
    }

    public static String findMostLimitNeighbor(Point point,ArrayList<Point> pointVec,ArrayList<Person> pVecDym,ArrayList<String> pVecDymName) {
        int sameDegree=pointVec.size();
        int diffDegree=pointVec.size();
        String mostSameNeighbor=null;
        String mostDiffNeighbor=null;
        Iterator<Point> poIterator = pointVec.iterator();
        while (poIterator.hasNext()) {
            Point thePoint = poIterator.next();
            double binary = pVecDym.get(pVecDymName.indexOf(point.name)).getPaid()
                    * pVecDym.get(pVecDymName.indexOf(thePoint.name)).getPaid();
            if (binary < 0){
                if (diffDegree > thePoint.neighbor.size()&& point.neighbor.indexOf(thePoint.name) != -1) {
                    diffDegree = thePoint.neighbor.size();
                    mostDiffNeighbor = thePoint.name;
                }
            }
            else {
                if(sameDegree > thePoint.neighbor.size()&& point.neighbor.indexOf(thePoint.name) != -1) {
                    sameDegree = thePoint.neighbor.size();
                    mostSameNeighbor = thePoint.name;
                }
            }
        }
        return (mostDiffNeighbor==null)?mostSameNeighbor:mostDiffNeighbor;
    }

    public static boolean hasLimit(String giver,String receiver,String[][] limit){
        for(int j=0;j<limit[0].length;j++){
            if((giver.equals(limit[0][j])&&receiver.equals(limit[1][j]))||(giver.equals(limit[1][j])&&receiver.equals(limit[0][j]))){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Solution> SettleTest(ArrayList<Person> pVec, ArrayList<Solution> sVec) throws UnableToClearException {
        ArrayList<Person> pVecDym;
        ArrayList<String> pVecName = new ArrayList<String>();
        ArrayList<String> pVecDymName;
        ArrayList<Solution> sVecDym;
        ArrayList<Point> pointVec = new ArrayList<Point>();
        ArrayList<String> pointVecName = new ArrayList<String>();
        String[][] limit;

        limit=new String[2][sVec.size()];

        Iterator<Person> pVecIterator=pVec.iterator();
        while(pVecIterator.hasNext())
            pVecName.add(pVecIterator.next().getName());
        sVecDym=(ArrayList<Solution>) sVec.clone();
        pVecDym = (ArrayList<Person>) pVec.clone();
        pVecDymName = (ArrayList<String>) pVecName.clone();

        Iterator<Solution> sVecDymIterator1=sVecDym.iterator();
        int i=0;
        while(sVecDymIterator1.hasNext()){
            Solution solu=new Solution(sVecDymIterator1.next());
            String giverName=solu.getGiver();
            String receiverName=solu.getReceiver();
            limit[0][i]=giverName;
            limit[1][i]=receiverName;
            Person giver=pVecDym.get(pVecDymName.indexOf(giverName));
            giver.setPaid(giver.getPaid()+solu.getAmount());
            Person receiver=pVecDym.get(pVecDymName.indexOf(receiverName));
            receiver.setPaid(receiver.getPaid()-solu.getAmount());
            i++;
        }

        ArrayList<Person> pVecToDelete=new ArrayList<Person>();
        Iterator<Person> pVecDymIterator=pVecDym.iterator();
        while(pVecDymIterator.hasNext()){
            Person person=pVecDymIterator.next();
            if(Math.abs(person.getPaid())<0.01)
                pVecToDelete.add(person);
        }
        Iterator<Person> pVecToDeleteIterator=pVecToDelete.iterator();
        while(pVecToDeleteIterator.hasNext()){
            Person person=pVecToDeleteIterator.next();
            pVecDymName.remove(person.getName());
            pVecDym.remove(person);
        }

        Iterator<Person> settleZeroIterator = pVecDym.iterator();
        while (settleZeroIterator.hasNext()) {
            Person person = settleZeroIterator.next();
            pointVec.add(new SettleModify().new Point(person.getName()));
            pointVecName.add(person.getName());
        }

        Iterator<Point> poIterator = pointVec.iterator();
        while (poIterator.hasNext()) {
            Point thePoint = poIterator.next();
            Iterator<String> pointVecNameIterator = pointVecName.iterator();
            while (pointVecNameIterator.hasNext()) {
                String neighbor = pointVecNameIterator.next();
                if(!hasLimit(thePoint.name, neighbor, limit)&&thePoint.name!=neighbor)
                    thePoint.neighbor.add(neighbor);
            }
        }

        boolean hasCompleteSolution=false;
        while(true){
            if (pVecDym.size()==0){
                hasCompleteSolution=true;
                break;
            }
            String mostLimit=findMostLimit(pointVec);
            if(mostLimit==null) break;
            String mostNeighbor=findMostLimitNeighbor(pointVec.get(pointVecName.indexOf(mostLimit)),pointVec,pVecDym,pVecDymName);
            if(mostNeighbor==null) break;

            if(pVecDym.get(pVecDymName.indexOf(mostLimit)).getPaid()*pVecDym.get(pVecDymName.indexOf(mostNeighbor)).getPaid()<0){
                Person maxPayer;
                Person minPayer;
                if(pVecDym.get(pVecDymName.indexOf(mostLimit)).getPaid()>0){
                    maxPayer=pVecDym.get(pVecDymName.indexOf(mostLimit));
                    minPayer=pVecDym.get(pVecDymName.indexOf(mostNeighbor));
                }
                else{
                    maxPayer=pVecDym.get(pVecDymName.indexOf(mostNeighbor));
                    minPayer=pVecDym.get(pVecDymName.indexOf(mostLimit));
                }
                Person newGiver = pVec
                        .get(pVecName.indexOf(minPayer.getName()));
                Person newReceiver = pVec.get(pVecName.indexOf(maxPayer
                        .getName()));
                if (maxPayer.getPaid() >= -minPayer.getPaid()+0.01) {
                    maxPayer.setPaid(maxPayer.getPaid() + minPayer.getPaid());
                    Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(),
                            -minPayer.getPaid());
                    sVecDym.add(newSolu);
                    pVecDymName.remove(minPayer.getName());
                    pVecDym.remove(minPayer);
                    settlePerson(pointVec.get(pointVecName.indexOf(minPayer.getName())), pointVec, pointVecName);
                } else if (maxPayer.getPaid()+0.01 <= -minPayer.getPaid()) {
                    minPayer.setPaid(maxPayer.getPaid() + minPayer.getPaid());
                    Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(),
                            maxPayer.getPaid());
                    sVecDym.add(newSolu);
                    pVecDymName.remove(maxPayer.getName());
                    pVecDym.remove(maxPayer);
                    settlePerson(pointVec.get(pointVecName.indexOf(maxPayer.getName())), pointVec, pointVecName);
                } else {
                    Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(),
                            maxPayer.getPaid());
                    sVecDym.add(newSolu);
                    pVecDymName.remove(maxPayer.getName());
                    pVecDymName.remove(minPayer.getName());
                    pVecDym.remove(maxPayer);
                    pVecDym.remove(minPayer);
                    settlePerson(pointVec.get(pointVecName.indexOf(minPayer.getName())), pointVec, pointVecName);
                    settlePerson(pointVec.get(pointVecName.indexOf(maxPayer.getName())), pointVec, pointVecName);
                }
            }
            else{
                Person outPerson=pVecDym.get(pVecDymName.indexOf(mostLimit));
                Person remainPerson=pVecDym.get(pVecDymName.indexOf(mostNeighbor));
                remainPerson.setPaid(remainPerson.getPaid()+outPerson.getPaid());

                Solution newSolu;
                if(outPerson.getPaid()>0)
                    newSolu = new Solution(remainPerson.getName(), outPerson.getName(),outPerson.getPaid());
                else
                    newSolu = new Solution(outPerson.getName(), remainPerson.getName(),-outPerson.getPaid());
                sVecDym.add(newSolu);
                pVecDymName.remove(outPerson.getName());
                pVecDym.remove(outPerson);
                settlePerson(pointVec.get(pointVecName.indexOf(outPerson.getName())), pointVec, pointVecName);
                if(Math.abs(remainPerson.getPaid())<0.01){
                    pVecDymName.remove(remainPerson.getName());
                    pVecDym.remove(remainPerson);
                    settlePerson(pointVec.get(pointVecName.indexOf(remainPerson.getName())), pointVec, pointVecName);
                }
            }
        }

        if(hasCompleteSolution) return sVecDym;
        else throw new UnableToClearException();
    }

}
