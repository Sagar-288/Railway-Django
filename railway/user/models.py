from django.db import models
from django_mysql.models import ListCharField
# Create your models here.


class User(models.Model):
	userId = models.CharField(primary_key=True, max_length=32)
	firstName = models.CharField(max_length=32)
	lastName = models.CharField(max_length=32)
	aadharNo = models.IntegerField()
	dateofbirth = models.DateField(null=True)
	email = models.EmailField(max_length=32, blank=True, null=True)
	telephone = models.CharField(max_length=16)
	country = models.CharField(max_length=32, blank=True, null=True)
	password = models.CharField(max_length=32, blank=True,null=True)


class Station(models.Model):
	stationId = models.CharField(primary_key = True, max_length=32)
	name = models.CharField(max_length=32)

class Train(models.Model):
	trainNo = models.IntegerField(primary_key = True, null=False)
	
	route = ListCharField(
        base_field=models.CharField(max_length=20),
        size=16,
        max_length=(16 * 21)  # 6 * 10 character nominals, plus commas
    )
	trainName = models.CharField(max_length=32)
	totalACSeats = models.IntegerField()
	totalSittingSeats = models.IntegerField()
	totalSleeperSeats = models.IntegerField()
	acfare = models.IntegerField()
	sittingfare = models.IntegerField()
	sleeperfare = models.IntegerField()


class TT(models.Model):
	userId = models.CharField(primary_key=True, max_length=32) 
	firstName = models.CharField(max_length=32)
	lastName = models.CharField(max_length=32)
	aadharNo = models.IntegerField() 

class TrainStatus(models.Model):
	trainNo = models.ForeignKey(Train, on_delete=models.CASCADE) 
	traindate = models.DateField()
	bookedACSeats = models.IntegerField(null=False)
	bookedSittingSeats = models.IntegerField(null=False)
	bookedSleeperSeats = models.IntegerField(null=False)

	def addAC(self):
		self.bookedACSeats = self.bookedACSeats + 1

	def addSleeper(self):
		self.bookedSleeperSeats += 1

	def addSitter(self):
		self.bookedSittingSeats += 1


# class Ticket(models.Model):
# 	ticketId = models.IntegerField(primary_key = True)
# 	bookedUser = models.ForeignKey(User, on_delete=models.CASCADE)
# 	status = models.CharField(max_length=32)
# 	noOfPassenger = models.IntegerField()

totalpassenger = 0
class Passenger(models.Model):

	passengerId = models.IntegerField(primary_key = True) 
	name = models.CharField(max_length=32, null=False)
	pnrNo = models.IntegerField(null=False)
	age = models.IntegerField(null=False)
	seatType = models.CharField(max_length=32, null=False)
	# reservationStatus = models.CharField(max_length=32)
	traindetails = models.ForeignKey(TrainStatus, on_delete=models.CASCADE, null=False)
	bookedBy = models.ForeignKey(User, on_delete=models.CASCADE)
	gender = models.CharField(max_length=32, null=False)

	def setPassengerID(self):
		global totalpassenger
		totalpassenger += 1
		self.passengerId = totalpassenger
		self.pnrNo = totalpassenger

	# ticketId = models.ForeignKey(Ticket, on_delete=models.CASCADE)
