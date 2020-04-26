from django.contrib import admin

from user.models import *
# Register your models here.


admin.site.register(User)
admin.site.register(Station)
admin.site.register(Train)
admin.site.register(TT)
admin.site.register(TrainStatus)
# admin.site.register(Ticket)
admin.site.register(Passenger)
