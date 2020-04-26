from django.shortcuts import render, redirect
from user.models import *
import hashlib
import json
from django.core.serializers.json import DjangoJSONEncoder
from django.core.serializers import serialize
from django.views.decorators.csrf import csrf_exempt
from django.http import JsonResponse
# Create your views here.
def index(request):
	return render(request, 'index.html')

@csrf_exempt
def login(request):
	print("HERE111")
	if request.method == "GET":
		return render(request, 'login.html')
	elif request.method == "POST":
		data = request.POST
		userId = data.get('userId')
		password = data.get('password')

		users = User.objects.filter(userId=userId)
		if not len(users):
			return render(request, 'login.html', {'error': 'incorrect user id'})

		if users[0].password != hashlib.md5(password.encode()).hexdigest():
			return render(request, 'login.html', {'error': 'incorrect password'})
		
		request.session.cycle_key()
		request.session['userId'] = userId
		
		return redirect('home')


@csrf_exempt
def AndriodLog(request):
	print("GEGE")
	if request.method == "GET":
		return JsonResponse({"status" : "error"})
	elif request.method == "POST":
		data = json.loads(request.body.decode())
		userId = data.get('userId')
		password = data.get('password')
		print(data)
		users = User.objects.filter(userId=userId)
		if not len(users):
			return JsonResponse({"status" : "error"})

		if users[0].password != hashlib.md5(password.encode()).hexdigest():
			return JsonResponse({"status" : "error"})

		request.session.cycle_key()
		request.session['userId'] = userId
		user = User.objects.filter(userId=userId)

		content = {
		'userId' : user[0].userId,
		'firstName': user[0].firstName,
		'lastName': user[0].lastName,
		'email': user[0].email,
		'telephone': user[0].telephone,
		'dateofbirth': user[0].dateofbirth,
		'country': user[0].country,
		'aadharNo': user[0].aadharNo
		}
		return JsonResponse({"status" : "success", 'userdetails' :content})



@csrf_exempt
def signup(request):
	if request.method == "GET":
		return render(request, 'signup.html')
	elif request.method == "POST":
		data = request.body
		data = json.loads(data.decode())
		userId = data.get('userId')
		if len(User.objects.filter(userId=userId)):
			return render(request, 'signup.html', {'error' : 'UserId already exists'})
		user = User()
		user.userId = userId
		user.firstName = data.get('firstName')
		user.lastName = data.get('lastName')
		user.aadharNo = data.get('aadharNo')
		# user.dateofbirth = data.get('dateofbirth')
		user.email = data.get('email')
		user.telephone = data.get('telephone')
		user.country = data.get('country')
		user.password = hashlib.md5(data.get('password').encode()).hexdigest()
		user.save()

		return redirect('index')


show = 0
result = []

def home(request):
	global show
	global result

	if request.method == "GET":
		print(request.method)
		userId = request.session.get('userId')
		if not userId:
			redirect('index')

		user = User.objects.filter(userId=userId)
		allbookings = Passenger.objects.filter(bookedBy=user[0])
		print(allbookings)
		context = {
			'userdetails' : user[0],
			'show' : show,
			'result': result,
			'bookings': allbookings
		}
		show = 0
		result = []
		return render(request, 'home.html', context)

	elif request.method == "POST":
		print("post request")
		data = request.POST
		if(data.get('forbooking') == "0"):
			date = data.get('date')
			source = data.get('from')
			dest = data.get('to')
			if(date and source and dest):
				show = 1
				print(date, source, dest)
				train = Train.objects.all()
				for i in train:
					if(source in i.route and dest in i.route):
						trainStatus = TrainStatus.objects.filter(trainNo=i.trainNo, traindate=date)
						

						if not len(trainStatus):
							trainStatus = TrainStatus()
							trainStatus.trainNo = i
							trainStatus.traindate = date
							trainStatus.bookedACSeats = 0
							trainStatus.bookedSittingSeats = 0
							trainStatus.bookedSleeperSeats = 0
							trainStatus.save()
							
							r = {
								'trainNo' : i.trainNo,
								'trainName': i.trainName,
								'availAC' : i.totalACSeats, 
								'availSitting' : i.totalSittingSeats, 
								'availSleeper' : i.totalSleeperSeats,
								'acfare' : i.acfare,
								'sittingfare' : i.sittingfare,
								'sleeperfare' : i.sleeperfare,
								'date': date
							}
							result += [r]
						else:
							r = {
								'trainNo' : i.trainNo,
								'trainName': i.trainName,
								'availAC' : i.totalACSeats - trainStatus[0].bookedACSeats,
								'availSitting' : i.totalSittingSeats - trainStatus[0].bookedSittingSeats,
								'availSleeper' : i.totalSleeperSeats - trainStatus[0].bookedSleeperSeats,
								'acfare' : i.acfare,
								'sittingfare' : i.sittingfare,
								'sleeperfare' : i.sleeperfare,
								'date' : date
							}
							result += [r]
			else:
				show = 0
		else:
			print(data,"entered")
			passenger = Passenger()
			passenger.setPassengerID()
			print(data.get('passname'))
			passenger.name = data.get('passname')
			passenger.age = data.get('passage')
			passenger.seatType = data.get('type')
			passenger.gender = data.get('gender')
			user = User.objects.filter(userId=data.get('senduser'))
			passenger.bookedBy = user[0] 
			trainstatus = TrainStatus.objects.filter(trainNo=data.get('sendnum'), traindate=data.get('senddate')) 

			t = data.get('type')
			print(trainstatus[0])
			if(t == "AC"):
				print("here")
				trainstatus[0].bookedACSeats += 1
			elif(t == "Sitting"):
				trainstatus[0].addSitter()
			else:
				trainstatus[0].addSleeper()
			
			trainstatus[0].save()
			passenger.traindetails = trainstatus[0]
			passenger.save()
			print(trainstatus[0].bookedACSeats)
		return redirect('home')


def logout(request):
	print(request)
	del request.session['userId']
	return redirect('index')

@csrf_exempt
def AndriodHome(request):
	userId = request.session['userId']

	if request.method == "GET":
		return JsonResponse({"status" : "error"})
	elif request.method == "POST":
		user = User.objects.filter(userId=userId)
		allbookings = Passenger.objects.filter(bookedBy=user[0])
		lst = {}
		count = 1
		for i in allbookings:
			content = {
			'traindate' : i.traindetails.traindate,
			'trainName': i.traindetails.trainNo.trainName,
			'trainNo': i.traindetails.trainNo.trainNo,
			'pnrNo': i.pnrNo,
			'seatType': i.seatType,
			'name': i.name,
			'age': i.age,
			'gender': i.gender
			}
			lst[count] = content
			count+=1
		return JsonResponse({"status" : "success", 'bookings' : lst})

@csrf_exempt
def AndriodLogout(request):
	del request.session['userId']
	return redirect('index')

@csrf_exempt
def AndriodBook(request):

	if request.method == "GET":
		return JsonResponse({"status" : "error"})

	elif request.method == "POST":
		print("post request")
		data = json.loads(request.body.decode())
		date = data.get('date')
		source = data.get('from')
		dest = data.get('to')
		count = 1
		lst = {}
		if(date and source and dest):
			train = Train.objects.all()

			for i in train:
				if(source in i.route and dest in i.route):
					trainStatus = TrainStatus.objects.filter(trainNo=i.trainNo, traindate=date)
					

					if not len(trainStatus):
						trainStatus = TrainStatus()
						trainStatus.trainNo = i
						trainStatus.traindate = date
						trainStatus.bookedACSeats = 0
						trainStatus.bookedSittingSeats = 0
						trainStatus.bookedSleeperSeats = 0
						trainStatus.save()
						
						r = {
							'trainNo' : i.trainNo,
							'trainName': i.trainName,
							'availAC' : i.totalACSeats, 
							'availSitting' : i.totalSittingSeats, 
							'availSleeper' : i.totalSleeperSeats,
							'acfare' : i.acfare,
							'sittingfare' : i.sittingfare,
							'sleeperfare' : i.sleeperfare,
							'date': date
						}
						lst[count] = r
						count+=1
					else:
						r = {
							'trainNo' : i.trainNo,
							'trainName': i.trainName,
							'availAC' : i.totalACSeats - trainStatus[0].bookedACSeats,
							'availSitting' : i.totalSittingSeats - trainStatus[0].bookedSittingSeats,
							'availSleeper' : i.totalSleeperSeats - trainStatus[0].bookedSleeperSeats,
							'acfare' : i.acfare,
							'sittingfare' : i.sittingfare,
							'sleeperfare' : i.sleeperfare,
							'date' : date
						}
						lst[count] = r
						count += 1
			print("lst", lst)
			return JsonResponse({'status':'success', 'listbook':lst})

		else:
			return JsonResponse({'status':'error'})


@csrf_exempt
def AndriodPassenger(request):

	if request.method == "GET":
		return JsonResponse({'status':'error'})

	elif request.method == "POST":
		print("post request")
		userId = request.session['userId']
		data = json.loads(request.body.decode())
		print(data,"entered")
		passenger = Passenger()
		passenger.setPassengerID()
		print(data.get('passname'))
		passenger.name = data.get('passname')
		passenger.age = data.get('passage')
		passenger.seatType = data.get('type')
		passenger.gender = data.get('gender')
		user = User.objects.filter(userId=userId)
		passenger.bookedBy = user[0] 
		trainstatus = TrainStatus.objects.filter(trainNo=data.get('sendnum'), traindate=data.get('senddate')) 

		t = data.get('type')
		print(trainstatus[0])
		if(t == "AC"):
			print("here")
			trainstatus[0].addAC()
		elif(t == "Sitting"):
			trainstatus[0].addSitter()
		else:
			trainstatus[0].addSleeper()
		
		trainstatus[0].save()
		passenger.traindetails = trainstatus[0]
		passenger.save()
		print(trainstatus[0].bookedACSeats)
		return JsonResponse({'status':'success'})