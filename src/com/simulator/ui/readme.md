# `ui/` Package — Person B's Workspace

> This folder is **Person B's territory**. Person A has set up the backend classes you'll depend on. This README explains what's available and how to wire everything together.

---

## Your Files to Create

| File | Purpose |
|---|---|
| `MainFrame.java` | JFrame window — arranges all panels below using layout managers |
| `TickerPanel.java` | Live price ticker strip (implements `PriceObserver`) |
| `ChartPanel.java` | Line chart via `paintComponent()` override (implements `PriceObserver`) |
| `TradePanel.java` | Quantity input + BUY / SELL buttons |
| `LedgerPanel.java` | JTable showing transaction history |
| `PortfolioSummaryPanel.java` | Displays cash, portfolio value, and P&L |

You also own **`Main.java`** (in the parent `com.simulator` package) — the application entry point.

---

## Backend Classes Available to You

### `com.simulator.observer.PriceObserver`
The shared interface. Your panels implement this to receive live price updates.
- Method: `void onPriceUpdate(Map<String, Double> prices)`
- Called every **1 second** by the engine
- Map keys are ticker strings like `"AAPL"`, `"BTC"`

### `com.simulator.model.Portfolio`
Holds cash balance and stock/crypto holdings.
- `double getCash()`
- `Map<String, Integer> getHoldings()`
- `List<Transaction> getHistory()`
- `double getTotalValue(Map<String, Double> livePrices)`

> Do NOT call `buy()` or `sell()` on Portfolio directly — always go through `TradeService`.

### `com.simulator.service.TradeService`
Validates and executes trades. Call from your `TradePanel` buttons.
- `executeBuy(portfolio, ticker, qty, livePrices)` → returns success/failure message
- `executeSell(portfolio, ticker, qty, livePrices)` → returns success/failure message

### `com.simulator.engine.MarketSubject`
The observer registry. Register your panels as observers here.
- `registerObserver(PriceObserver observer)`
- `removeObserver(PriceObserver observer)`

### `com.simulator.engine.PriceEngine`
The simulation driver. Start/stop from `Main.java`.
- `start()` — begins the 1-second simulation loop
- `stop()` — shuts down the scheduler (call on window close)

---

## Thread Safety Reminder

`onPriceUpdate()` is called from a **background thread**, not the Swing EDT. Wrap all Swing updates inside:

```java
SwingUtilities.invokeLater(() -> {
    // update your labels, repaint charts, etc.
});
```

---

## Wiring Checklist (for Main.java)

1. Create `Portfolio` (starting cash, e.g. 100,000)
2. Create assets (`Stock` / `Crypto` objects with tickers & initial prices)
3. Create `MarketSubject` and add all assets to it
4. Create `PriceEngine` with the `MarketSubject`
5. Create all UI panels, passing in `Portfolio` / `TradeService` refs as needed
6. Register `TickerPanel` and `ChartPanel` as observers via `marketSubject.registerObserver()`
7. Assemble panels inside `MainFrame`
8. Call `priceEngine.start()`
9. Add a window close listener that calls `priceEngine.stop()`

---

## Questions?

Reach out to Person A if any method signature doesn't match what you expect — the backend API is final once both sides agree on it.
