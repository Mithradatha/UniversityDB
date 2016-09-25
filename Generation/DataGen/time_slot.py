class Time_Slot:
    count = 0
    
    def __init__(self, time_slot_id, days, start_time, end_time):
        self.ID = time_slot_id
        self.days = days
        self.start_time = start_time
        self.end_time = end_time
        Time_Slot.count += 1

    def headers(self):
        return 'ID,days,start_time,end_time\n'
    
    def printLine(self):
        return '{0},{1},{2},{3}\n'.format(self.ID, self.days, self.start_time, self.end_time)
    

time_slots = [
    
    Time_Slot(1,'MWF','8:00:00','8:50:00'),
    Time_Slot(2,'MWF','9:00:00','9:50:00'),
    Time_Slot(3,'MWF','10:00:00','10:50:00'),
    Time_Slot(4,'MWF','11:00:00','11:50:00'),
    Time_Slot(5,'MWF','12:00:00','12:50:00'),
    Time_Slot(6,'MWF','13:00:00','13:50:00'),
    Time_Slot(7,'MWF','14:00:00','14:50:00'),
    Time_Slot(8,'MWF','15:00:00','15:50:00'),
    Time_Slot(9,'MWF','16:00:00','16:50:00'),
    Time_Slot(10,'MWF','17:00:00','17:50:00'),
    Time_Slot(11,'TR','8:00:00','9:15:00'),
    Time_Slot(12,'TR','9:30:00','10:45:00'),
    Time_Slot(13,'TR','11:00:00','12:15:00'),
    Time_Slot(14,'TR','12:30:00','13:45:00'),
    Time_Slot(15,'TR','14:00:00','15:15:00'),
    Time_Slot(16,'TR','15:30:00','16:45:00'),
    Time_Slot(17,'TR','17:00:00','18:15:00')
]
    