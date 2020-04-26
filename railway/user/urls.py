
from django.conf.urls import url
from django.contrib import admin
from . import views

urlpatterns = [
	url("index", views.index, name="index"),
	url("login", views.login, name="login"),
	url("AndriodLog", views.AndriodLog, name="AndriodLog"),
	url("AndriodHome", views.AndriodHome, name="AndriodHome"),
	url("AndriodLogout", views.AndriodLogout, name="AndriodLogout"),
	url("AndriodBook", views.AndriodBook, name="AndriodBook"),
	url("AndriodPassenger", views.AndriodPassenger, name="AndriodPassenger"),
	url("signup",views.signup, name="signup"),
	url("home", views.home, name="home"),
	url("logout", views.logout, name="logout"),
]
