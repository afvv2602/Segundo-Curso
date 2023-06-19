from django.db import models
from django.contrib.auth.models import User

class Doctor(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='doctor_profile')

    def __str__(self):
        return self.user.username

class Paciente(models.Model):
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='patient_profile')

    def __str__(self):
        return self.user.username

class Perfil(models.Model):
    TIPO_USUARIO_CHOICES = [
        ('PAC', 'Paciente'),
        ('DOC', 'Doctor'),
    ]
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    tipo_usuario = models.CharField(max_length=3, choices=TIPO_USUARIO_CHOICES)

class Pregunta(models.Model):
    titulo = models.CharField(max_length=200)
    descripcion = models.TextField()
    paciente = models.ForeignKey(Paciente, related_name='preguntas', on_delete=models.CASCADE)
    medico = models.ForeignKey(Doctor, related_name='respuestas', on_delete=models.SET_NULL, null=True)
    respuesta = models.TextField(blank=True)

class Cita(models.Model):
    fecha = models.DateField()
    hora = models.TimeField()
    medico = models.ForeignKey(Doctor, related_name='citas_doctor', on_delete=models.CASCADE)
    paciente = models.ForeignKey(Paciente, related_name='citas_paciente', on_delete=models.CASCADE)
