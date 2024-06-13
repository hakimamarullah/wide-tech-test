/*
@Author hakim a.k.a. Hakim Amarullah
Java Developer
Created on 6/12/2024 5:52 PM
@Last Modified 6/12/2024 5:52 PM
Version 1.0
*/

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MyGasStation {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        GasStation gasStation = new PertamaxStation();
        int totalCustomer = sc.nextInt();

        sc.nextLine();
        while (totalCustomer-- > 0) {
            gasStation.registerCustomer(sc.nextLine());
        }

        while (sc.hasNextLine()) {
            String command = sc.nextLine();
            if (command.isEmpty()) {
                break;
            }

            try {
                if (command.startsWith("isi")) {
                    System.out.println(gasStation.refuel(command));
                } else if (command.equalsIgnoreCase("data pembeli")) {
                    gasStation.showCustomerData();
                } else if (command.equalsIgnoreCase("data transaksi")) {
                    gasStation.showTransactions();
                } else if (command.startsWith("total")) {
                    gasStation.showTotalTransactionsByVehicleType(command.split(";")[1]);
                } else if (command.startsWith("refill")) {
                    String customerKey = command.split(";")[1];
                    String[] data = customerKey.split("-");
                    gasStation.refillCustomerBalance(data[0], data[1], 10);
                } else if (command.startsWith("cek")) {
                    gasStation.checkFuelBalanceByCustomerName(command.split(";")[1]);

                } else {
                    System.out.println("Unknown Command");
                }

            } catch (Exception e) {
                System.out.printf("System failure: %s%n", e.getMessage());
            }
        }
        sc.close();
    }
}


interface GasStation {
    void registerCustomer(String s);

    String refuel(String command);

    void showCustomerData();

    void showTransactions();

    void showTotalTransactionsByVehicleType(String s);

    void refillCustomerBalance(String customerName, String vehicleType, int volume);

    void checkFuelBalanceByCustomerName(String customerName);
}

class PertamaxStation implements GasStation {
    public static final String MOTOR = "motor";
    private final Map<String, Customer> customers = new HashMap<>();

    private final List<Transaction> transactions = new ArrayList<>();
    private final AtomicInteger motorVolume = new AtomicInteger(0);
    private final AtomicInteger mobilVolume = new AtomicInteger(0);

    public void registerCustomer(String input) {
        String[] customerData = input.split("-");
        Vehicle vehicle = Vehicle.createVehicle(customerData[1]);
        Customer customer = this.customers.getOrDefault(customerData[0], new Customer(customerData[0]));
        customer.addVehicle(vehicle);

        this.customers.put(customerData[0], customer);
    }


    public void refillCustomerBalance(String customerName, String vehicleType, int volume) {
        Customer customer = this.customers.get(customerName);
        if (customer == null) {
            System.out.println("Nama dan kendaraan belum terdaftar");
            return;
        }

        Vehicle vehicle = customer.getVehicleByType(vehicleType);
        vehicle.refillPertamaxBalance(volume);

        System.out.printf("%s berhasil melakukan pengisian ulang sebanyak %d liter%n", customerName, vehicle.getPertamaxBalance());

    }

    public void checkFuelBalanceByCustomerName(String customerName) {
        Customer customer = this.customers.get(customerName);
        if (customer == null) {
            System.out.println("Nama tersebut belum terdaftar");
            return;
        }

        int motorBalance = Optional.ofNullable(customer.getMotor())
                .map(Vehicle::getPertamaxBalance)
                .orElse(0);
        int mobilBalance = Optional.ofNullable(customer.getMobil())
                .map(Vehicle::getPertamaxBalance)
                .orElse(0);
        if (motorBalance != 0) {
            System.out.printf("Motor : %d liter%n", customer.getMotor().getPertamaxBalance());
        }

        if (mobilBalance != 0) {
            System.out.printf("Mobil : %d liter%n", customer.getMobil().getPertamaxBalance());
        }
    }

    public String refuel(String command) {
        String[] refuelData = command.split(";");
        int volume = Integer.parseInt(refuelData[2]);
        String[] customerKey = refuelData[1].split("-");
        String vehicleType = customerKey[1];

        Customer customer = this.customers.get(customerKey[0]);

        if (customer == null) {
            return "Nama dan kendaraan belum terdaftar";
        }

        Vehicle vehicle = customer.getVehicleByType(vehicleType);
        boolean isCharged = vehicle.chargePertamaxBalance(volume);

        if (!isCharged) {
            return "Maaf, sisa pertamax tidak mencukupi";
        }


        logTransaction(customer, vehicleType, volume);
        if (MOTOR.equals(vehicleType)) {
            this.motorVolume.getAndAdd(volume);
        } else {
            this.mobilVolume.getAndAdd(volume);
        }

        return String.format("Berhasil melakukan pengisian %s[%s]", vehicleType, customer.getName());
    }

    public void showCustomerData() {
        List<Customer> registeredCustomers = new ArrayList<>(this.customers.values());
        Collections.sort(registeredCustomers);

        String outputFormat = "%d. %s | jenis %5s | sisa maksimal pengisian:%d liter%n";
        int numbering = 1;
        for (Customer customer : registeredCustomers) {

            if (customer.getVehicleByType("mobil") != null) {
                Vehicle mobil = customer.getMobil();
                System.out.printf((outputFormat), numbering++, customer.getName(), mobil, mobil.getPertamaxBalance());
            }

            if (customer.getVehicleByType(MOTOR) != null) {
                Vehicle motor = customer.getMotor();
                System.out.printf((outputFormat), numbering++, customer.getName(), motor, motor.getPertamaxBalance());
            }


        }
    }

    public void showTransactions() {
        if (this.transactions.isEmpty()) {
            System.out.println("Belum ada transaksi apapun");
            return;
        }
        this.transactions.sort(new TransactionDateComparator());

        String outputFormat = "%d. %s mengisi bensin jenis %s sebanyak %d liter%n";
        int numbering = 1;


        for (Transaction transaction : transactions) {
            System.out.printf((outputFormat), numbering++, transaction.getCustomerName(), transaction.getVehicleType(), transaction.getVolume());
        }
    }


    public void showTotalTransactionsByVehicleType(String vehicleType) {
        List<Transaction> filteredTransaction = this.transactions.stream()
                .filter(item -> item.getVehicleType().equals(vehicleType))
                .sorted(new TransactionDateComparator())
                .collect(Collectors.toList());

        Map<String, Integer> totalPerCustomer = new HashMap<>();
        for (Transaction transaction : filteredTransaction) {
            int currentVolume = totalPerCustomer.getOrDefault(transaction.getCustomerName(), 0);
            totalPerCustomer.put(transaction.getCustomerName(), currentVolume + transaction.getVolume());
        }

        for (Transaction x : filteredTransaction) {
            System.out.printf("%s telah mengisi: %d liter%n", x.getCustomerName(), totalPerCustomer.get(x.getCustomerName()));
        }

        int total = vehicleType.equalsIgnoreCase(MOTOR) ? motorVolume.get() : mobilVolume.get();
        System.out.printf("Total pengisian %s: %d liter%n", vehicleType, total);
    }

    private void logTransaction(Customer customer, String vehicleType, int volume) {
        Transaction newTransaction = new Transaction();
        newTransaction.setCustomerName(customer.getName());
        newTransaction.setVehicleType(vehicleType);
        newTransaction.setCreatedAt(LocalDateTime.now());
        newTransaction.setVolume(volume);

        this.transactions.add(newTransaction);
    }
}

class TransactionDateComparator implements Comparator<Transaction> {

    @Override
    public int compare(Transaction o1, Transaction o2) {
        return o1.getCreatedAt().compareTo(o2.getCreatedAt());
    }
}

class Transaction {
    private String customerName;
    private String vehicleType;
    private Integer volume;
    private LocalDateTime createdAt;

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

class Customer implements Comparable<Customer> {

    public static final String MOTOR = "motor";
    private final Vehicle[] vehicles = new Vehicle[2];
    private final String name;

    public Customer(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Vehicle getMotor() {
        return vehicles[0];
    }

    public void setMotor(Vehicle motor) {
        this.vehicles[0] = motor;
    }

    public Vehicle getMobil() {
        return this.vehicles[1];
    }

    public void setMobil(Vehicle mobil) {
        this.vehicles[1] = mobil;
    }


    public void addVehicle(Vehicle vehicle) {
        if (MOTOR.equalsIgnoreCase(vehicle.getVehicleType())) {
            this.setMotor(vehicle);
            return;
        }
        this.setMobil(vehicle);
    }

    public Vehicle getVehicleByType(String vehicleType) {
        if (MOTOR.equalsIgnoreCase(vehicleType)) {
            return this.getMotor();
        }
        return this.getMobil();
    }

    @Override
    public int compareTo(Customer other) {
        return this.name.compareTo(other.name);
    }
}


abstract class Vehicle {
    public static final String MOTOR = "motor";
    private Integer pertamaxBalance;

    protected Vehicle(Integer pertamaxBalance) {
        this.pertamaxBalance = pertamaxBalance;
    }

    public static Vehicle createVehicle(String vehicleType) {
        if (MOTOR.equalsIgnoreCase(vehicleType)) {
            return new Motor();
        }
        return new Mobil();
    }

    public Integer getPertamaxBalance() {
        return pertamaxBalance;
    }

    public boolean chargePertamaxBalance(int amount) {
        if (amount > this.pertamaxBalance) {
            return false;
        }

        this.pertamaxBalance -= amount;
        return true;
    }

    public void refillPertamaxBalance(int refillAmount) {
        this.pertamaxBalance += refillAmount;
    }

    public abstract String getVehicleType();

    public String toString() {
        return getVehicleType();
    }
}

class Motor extends Vehicle {

    public Motor() {
        super(10);
    }

    @Override
    public String getVehicleType() {
        return "motor";
    }
}

class Mobil extends Vehicle {

    public Mobil() {
        super(50);
    }

    @Override
    public String getVehicleType() {
        return "mobil";
    }
}
