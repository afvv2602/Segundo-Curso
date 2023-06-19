from django.urls import path
from . import views

app_name = 'clinic'

urlpatterns = [
    path('login/', views.LoginView.as_view(), name='login'),
    path('logout/', views.LogoutView.as_view(), name='logout'),
    path('doctor_virtual/', views.DoctorVirtualView.as_view(), name='doctor_virtual'),
    path('doctor_virtual/respuesta/<int:pk>/', views.ResponderPreguntaView.as_view(), name='responder_pregunta'),
    path('doctor_virtual/nueva/', views.NuevaPreguntaView.as_view(), name='nueva_pregunta'),
    path('citas/', views.CitasView.as_view(), name='citas'),
    path('citas/cancelar/<int:pk>/', views.CancelarCitaView.as_view(), name='cancelar_cita'),
    path('citas/nueva/', views.NuevaCitaView.as_view(), name='nueva_cita'),
]
