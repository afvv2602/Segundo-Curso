from django.db import models
from django.contrib.auth.models import User

class Question(models.Model):
    title = models.CharField(max_length=200)
    description = models.TextField()
    answered = models.BooleanField(default=False)
    user = models.ForeignKey(User, on_delete=models.CASCADE)

class Appointment(models.Model):
    date = models.DateField()
    time = models.TimeField()
    specialty = models.CharField(max_length=100)
    doctor = models.CharField(max_length=100)
    user = models.ForeignKey(User, on_delete=models.CASCADE)