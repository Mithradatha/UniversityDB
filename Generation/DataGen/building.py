class Building:
    count = 0

    def __init__(self, rooms):
        Building.count += 1
        self.number = Building.count
        self.rooms = rooms