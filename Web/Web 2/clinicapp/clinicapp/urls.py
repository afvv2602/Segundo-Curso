from django.urls import path
from clinic.views import doctor_virtual, citas
from django.contrib.auth import views as auth_views


urlpatterns = [
    path('', doctor_virtual, name='home'),
        path('login/', auth_views.LoginView.as_view(), name='login'),
    path('doctor_virtual/', doctor_virtual, name='doctor_virtual'),
    path('citas/', citas, name='citas'),
]
