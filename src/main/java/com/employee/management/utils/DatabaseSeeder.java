package com.employee.management.utils;

import com.employee.management.building.BuildingRepository;
import com.employee.management.city.CityRepository;
import com.employee.management.country.CountryRepository;
import com.employee.management.department.DepartmentRepository;
import com.employee.management.employee.EmployeeRepository;
import com.employee.management.entity.*;
import com.employee.management.location.LocationRepository;
import com.employee.management.team.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
@Profile("dev") // Only runs when spring.profiles.active=dev
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;
    private final LocationRepository locationRepository;
    private final BuildingRepository buildingRepository;
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (countryRepository.count() > 0) {
            log.info("Database already seeded. Skipping data generation.");
            return;
        }

        log.info("Starting database seeding: Generating 100 rows per entity...");
        int TARGET_COUNT = 100;
        Random random = new Random();

        // 1. Seed 100 Countries
        List<Country> countries = new ArrayList<>();
        for (int i = 1; i <= TARGET_COUNT; i++) {
            Country country = new Country();
            country.setName("Country " + i);
            // Ensures exactly 2 characters for the ISO code constraint
            country.setIsoCode(String.format("%02d", i % 100));
            countries.add(country);
        }
        countries = countryRepository.saveAll(countries);
        log.info("✅ Inserted 100 Countries");

        // 2. Seed 100 Cities
        List<City> cities = new ArrayList<>();
        for (int i = 1; i <= TARGET_COUNT; i++) {
            City city = new City();
            city.setName("City " + i);
            city.setCountry(countries.get(random.nextInt(countries.size())));
            cities.add(city);
        }
        cities = cityRepository.saveAll(cities);
        log.info("✅ Inserted 100 Cities");

        // 3. Seed 100 Locations
        List<Location> locations = new ArrayList<>();
        for (int i = 1; i <= TARGET_COUNT; i++) {
            Location location = new Location();
            location.setStreetAddress(i + " Main Street");
            location.setPostalCode("ZIP-" + i);
            location.setCity(cities.get(random.nextInt(cities.size())));
            locations.add(location);
        }
        locations = locationRepository.saveAll(locations);
        log.info("✅ Inserted 100 Locations");

        // 4. Seed 100 Buildings
        List<Building> buildings = new ArrayList<>();
        for (int i = 1; i <= TARGET_COUNT; i++) {
            Building building = new Building();
            building.setName("Tower " + i);
            building.setLocation(locations.get(random.nextInt(locations.size())));
            buildings.add(building);
        }
        buildings = buildingRepository.saveAll(buildings);
        log.info("✅ Inserted 100 Buildings");

        // 5. Seed 100 Departments
        List<Department> departments = new ArrayList<>();
        String[] deptPrefixes = {"Engineering", "HR", "Finance", "Marketing", "Operations"};
        for (int i = 1; i <= TARGET_COUNT; i++) {
            Department department = new Department();
            department.setName(deptPrefixes[random.nextInt(deptPrefixes.length)] + " Dept " + i);
            department.setBuilding(buildings.get(random.nextInt(buildings.size())));
            departments.add(department);
        }
        departments = departmentRepository.saveAll(departments);
        log.info("✅ Inserted 100 Departments");

        // 6. Seed 100 Teams
        List<Team> teams = new ArrayList<>();
        for (int i = 1; i <= TARGET_COUNT; i++) {
            Team team = new Team();
            team.setName("Alpha Squad " + i);
            team.setDepartment(departments.get(random.nextInt(departments.size())));
            teams.add(team);
        }
        teams = teamRepository.saveAll(teams);
        log.info("✅ Inserted 100 Teams");

        // 7. Seed 100 Employees with Realistic Names (Allowing natural name repeats)
        List<Employee> employees = new ArrayList<>();
        String[] firstNames = {"James", "Mary", "Michael", "Patricia", "Robert", "Jennifer", "John", "Linda", "David", "Elizabeth", "William", "Barbara", "Richard", "Susan", "Joseph", "Jessica", "Thomas", "Sarah", "Charles", "Karen"};
        String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson", "Thomas", "Taylor", "Moore", "Jackson", "Martin"};

        for (int i = 1; i <= TARGET_COUNT; i++) {
            String fName = firstNames[random.nextInt(firstNames.length)];
            String lName = lastNames[random.nextInt(lastNames.length)];

            Employee employee = new Employee();
            employee.setFirstName(fName);
            employee.setLastName(lName);

            // Unique email guaranteed by appending 'i'
            employee.setEmail(fName.toLowerCase() + "." + lName.toLowerCase() + i + "@company.com");
            employee.setRole(i % 2 == 0 ? EmployeeRole.SENIOR : EmployeeRole.JUNIOR);

            // Assign to a random department
            Department assignedDept = departments.get(random.nextInt(departments.size()));
            employee.setDepartment(assignedDept);

            // Find teams belonging to this specific department
            List<Team> availableTeamsForDept = teams.stream()
                    .filter(t -> t.getDepartment().getId().equals(assignedDept.getId()))
                    .toList();

            // Assign to a team if available
            if (!availableTeamsForDept.isEmpty()) {
                employee.addTeam(availableTeamsForDept.get(random.nextInt(availableTeamsForDept.size())));
            }

            employees.add(employee);
        }

        employeeRepository.saveAll(employees);
        log.info("✅ Inserted 100 Employees with Realistic Names");

        log.info("🚀 Database seeding complete! 700 rows generated successfully.");
    }
}
