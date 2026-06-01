# Parking Lot System (LLD)

A **Java 8** low-level design implementation of a multi-floor parking lot for machine coding interviews. The project models real-world flows: entry tickets, tiered hourly billing, multiple payment channels, capacity enforcement, and per-floor availability displays.

## Features

| Requirement | Implementation |
|-------------|----------------|
| Multiple floors | `ParkingFloor` with configurable spots per floor |
| Multiple entry/exit points | `EntryPanel`, `ExitPanel` (automated + attendant) |
| Parking tickets at entry | `ParkingTicket` issued by `EntryPanel` |
| Pay at exit (panel or attendant) | `ExitPanel` with `automated` flag |
| Cash & credit card | `PaymentMethod` enum |
| Pay at floor info portal | `CustomerInfoPortal` — skip exit payment if already paid |
| Capacity limit | `ParkingService.isFull()` blocks entry; message on entry panel & ground display |
| Spot types | `COMPACT`, `LARGE`, `HANDICAPPED`, `MOTORCYCLE`, `ELECTRIC` |
| Electric charging | `ElectricPanel` on electric spots + `ElectricChargingPanel` |
| Vehicle types | `CAR`, `TRUCK`, `VAN`, `MOTORCYCLE`, `ELECTRIC_CAR` |
| Per-floor display boards | `DisplayBoard` updated on park/unpark |
| Tiered hourly fees | `ParkingFeeCalculator` — $4 / $3.5 / $3.5 / $2.5+ |

### Fee Model

- **1st hour:** $4.00  
- **2nd & 3rd hours:** $3.50 each  
- **4th hour onward:** $2.50 each  

Partial hours are rounded up to the next full hour.

## Project Structure

```
src/main/java/com/parkinglot/
├── Main.java                    # Demo scenarios
├── ParkingLot.java              # System facade
├── enums/                       # VehicleType, ParkingSpotType, PaymentMethod, PaymentStatus
├── model/                       # Vehicle, ParkingSpot, ParkingFloor, ParkingTicket, DisplayBoard
├── panel/                       # EntryPanel, ExitPanel, CustomerInfoPortal, GroundFloorDisplayBoard
├── pricing/                     # ParkingFeeCalculator
├── service/                     # ParkingService, PaymentService
├── strategy/                    # SpotAllocationStrategy
├── factory/                     # ParkingLotFactory (sample configuration)
└── exception/                   # ParkingLotException
```

## Prerequisites

- **Java 8** or higher  
- **Apache Maven 3.6+**

## Build

```bash
mvn clean package
```

This produces:

- `target/parking-lot-lld-1.0.0.jar`
- `target/parking-lot-lld-1.0.0-jar-with-dependencies.jar` (executable fat JAR)

## Run

```bash
java -jar target/parking-lot-lld-1.0.0-jar-with-dependencies.jar
```

Or use the provided script:

```bash
chmod +x build-and-run.sh
./build-and-run.sh
```

## Design Highlights

1. **Facade pattern** — `ParkingLot` coordinates entry, exit, payment, and displays.  
2. **Strategy pattern** — `SpotAllocationStrategy` maps vehicle types to preferred spot types.  
3. **Separation of concerns** — parking logic (`ParkingService`) vs billing (`PaymentService`) vs UI panels.  
4. **Capacity as invariant** — checked before issuing tickets; ground board reflects full state.  
5. **Payment state on ticket** — `PaymentStatus` enables info-portal prepay and exit skip.  
6. **Extensibility** — add floors/spots via `ParkingLot.Builder` and `ParkingLotFactory`.

## Example Demo Output

The `Main` class runs five scenarios:

1. Park car → pay at automated exit (credit card)  
2. Park van → pay at floor info portal → exit without double payment  
3. Electric car → charging panel payment → exit  
4. Fill lot → display “PARKING FULL” → reject overflow entry  
5. Tiered fee calculation for ~5.5 hours  

## License

Educational / interview preparation use.
