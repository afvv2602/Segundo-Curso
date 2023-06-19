from django.urls import path
from clinic.views import doctor_virtual, citas
from django.contrib.auth import views as auth_views


urlpatterns = [
    path('', auth_views.LoginView.as_view(template_name='registration/login.html'), name='home'),
    path('doctor_virtual/', doctor_virtual, name='doctor_virtual'),
    path('citas/', citas, name='citas'),
]
