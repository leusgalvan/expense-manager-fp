package expenses

class MapDebtStoreSpec
    extends DebtStoreSpec(new MapDebtStore(Map.empty), "MapDebtStore")
