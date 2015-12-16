package oneBill.domain.entity;

import java.util.Iterator;
import java.util.ArrayList;

import oneBill.domain.entity.error.UnableToClearException;

/**
 * Created by 豪豪 on 2015/12/16.
 */
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

    public static void settlePerson(Point point, ArrayList<Point> pointVec,
                                    ArrayList<String> pointVecName) {
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

    public static String findMostLimitNeighbor(Point point,
                                               ArrayList<Point> pointVec) {
        int degree = pointVec.size();
        String mostNeighbor = null;
        Iterator<Point> poIterator = pointVec.iterator();
        while (poIterator.hasNext()) {
            Point thePoint = poIterator.next();
            if (degree > thePoint.neighbor.size()
                    && point.neighbor.indexOf(thePoint.name) != -1) {
                degree = thePoint.neighbor.size();
                mostNeighbor = thePoint.name;
            }
        }
        return mostNeighbor;
    }

    public ArrayList<Solution> SettleTest(ArrayList<Person> pVec, ArrayList<Solution> sVec) throws UnableToClearException {
        ArrayList<Person> pVecDym;
        ArrayList<String> pVecName = new ArrayList<String>();
        ArrayList<String> pVecDymName;
        ArrayList<Solution> sVecDym;
        ArrayList<Point> pointVec = new ArrayList<Point>();
        ArrayList<String> pointVecName = new ArrayList<String>();

        Iterator<Person> pVecIterator = pVec.iterator();
        while (pVecIterator.hasNext())
            pVecName.add(pVecIterator.next().getName());
        pVecDym = (ArrayList<Person>) pVec.clone();
        pVecDymName = (ArrayList<String>) pVecName.clone();
        sVecDym = (ArrayList<Solution>) sVec.clone();

        Iterator<Person> settleZeroIterator = pVec.iterator();
        while (settleZeroIterator.hasNext()) {
            Person person = settleZeroIterator.next();
            if (Math.abs(person.getPaid()) < 0.01) {
                pVecDym.remove(person);
                pVecDymName.remove(person.getName());
            } else {
                pointVec.add(new SettleModify().new Point(person.getName()));
                pointVecName.add(person.getName());
            }
        }

        Iterator<Point> poIterator = pointVec.iterator();
        while (poIterator.hasNext()) {
            Point thePoint = poIterator.next();
            Iterator<String> pointVecNameIterator = pointVecName.iterator();
            while (pointVecNameIterator.hasNext()) {
                String neighbor = pointVecNameIterator.next();
                double binary = pVecDym.get(pVecDymName.indexOf(thePoint.name))
                        .getPaid()
                        * pVecDym.get(pVecDymName.indexOf(neighbor)).getPaid();
                if (binary < 0)
                    thePoint.neighbor.add(neighbor);
            }
        }

        Iterator<Solution> sVecDymIterator1 = sVecDym.iterator();
        int i = 0;
        while (sVecDymIterator1.hasNext()) {
            Solution solu = new Solution(sVecDymIterator1.next());
            String giverName = solu.getGiver();
            String receiverName = solu.getReceiver();
            pointVec.get(pointVecName.indexOf(giverName)).neighbor
                    .remove(receiverName);
            Person giver = pVecDym.get(pVecDymName.indexOf(giverName));
            giver.setPaid(giver.getPaid() + solu.getAmount());
            if (Math.abs(giver.getPaid()) < 0.01) {
                pVecDymName.remove(giver.getName());
                pVecDym.remove(giver);
                settlePerson(pointVec.get(pointVecName.indexOf(giverName)),
                        pointVec, pointVecName);
            }
            else
                pointVec.get(pointVecName.indexOf(receiverName)).neighbor
                        .remove(giverName);
            Person receiver = pVecDym.get(pVecDymName.indexOf(receiverName));
            receiver.setPaid(receiver.getPaid() - solu.getAmount());
            if (Math.abs(receiver.getPaid()) <= 0.01) {
                pVecDymName.remove(receiver.getName());
                pVecDym.remove(receiver);
                settlePerson(pointVec.get(pointVecName.indexOf(receiverName)),
                        pointVec, pointVecName);
            }
            else {
                if (giver.getPaid() != 0)
                    pointVec.get(pointVecName.indexOf(giverName)).neighbor
                            .remove(receiverName);
            }
            i++;
        }

        boolean hasCompleteSolution=false;
        while(true){
            if (pVecDym.size()==0){
                hasCompleteSolution=true;
                break;
            }
            String mostLimit=findMostLimit(pointVec);
            if(mostLimit==null) break;
            String mostNeighbor=findMostLimitNeighbor(pointVec.get(pointVecName.indexOf(mostLimit)),pointVec);
            if(mostNeighbor==null) break;

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

            if (maxPayer.getPaid() > -minPayer.getPaid()) {
                maxPayer.setPaid(maxPayer.getPaid() + minPayer.getPaid());
                Person newGiver = pVec
                        .get(pVecName.indexOf(minPayer.getName()));
                Person newReceiver = pVec.get(pVecName.indexOf(maxPayer
                        .getName()));
                Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(),
                        -minPayer.getPaid());
                sVecDym.add(newSolu);
                pVecDymName.remove(minPayer.getName());
                pVecDym.remove(minPayer);
                settlePerson(pointVec.get(pointVecName.indexOf(minPayer.getName())), pointVec, pointVecName);
            } else if (maxPayer.getPaid() < -minPayer.getPaid()) {
                minPayer.setPaid(maxPayer.getPaid() + minPayer.getPaid());
                Person newGiver = pVec
                        .get(pVecName.indexOf(minPayer.getName()));
                Person newReceiver = pVec.get(pVecName.indexOf(maxPayer
                        .getName()));
                Solution newSolu = new Solution(newGiver.getName(), newReceiver.getName(),
                        maxPayer.getPaid());
                sVecDym.add(newSolu);
                pVecDymName.remove(maxPayer.getName());
                pVecDym.remove(maxPayer);
                settlePerson(pointVec.get(pointVecName.indexOf(maxPayer.getName())), pointVec, pointVecName);
            } else {
                Person newGiver = pVec
                        .get(pVecName.indexOf(minPayer.getName()));
                Person newReceiver = pVec.get(pVecName.indexOf(maxPayer
                        .getName()));
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

        if(hasCompleteSolution) return sVecDym;
        else throw new UnableToClearException();
    }
}
